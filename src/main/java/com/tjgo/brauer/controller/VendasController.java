package com.tjgo.brauer.controller;

import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tjgo.brauer.controller.page.PageWrapper;
import com.tjgo.brauer.controller.validator.VendaValidator;
import com.tjgo.brauer.dto.VendaMes;
import com.tjgo.brauer.dto.VendaOrigem;
import com.tjgo.brauer.mail.Mailer;
import com.tjgo.brauer.model.Cerveja;
import com.tjgo.brauer.model.ItemVenda;
import com.tjgo.brauer.model.StatusVenda;
import com.tjgo.brauer.model.TipoPessoa;
import com.tjgo.brauer.model.Venda;
import com.tjgo.brauer.repository.Cervejas;
import com.tjgo.brauer.repository.Vendas;
import com.tjgo.brauer.repository.filter.VendaFilter;
import com.tjgo.brauer.security.UsuarioSistema;
import com.tjgo.brauer.service.CadastroVendaService;
import com.tjgo.brauer.session.TabelaItensSession;

@Controller
@RequestMapping("/vendas")
public class VendasController {
	
	@Autowired
	private Cervejas cervejas;
	
	@Autowired
	private TabelaItensSession tabelaItens;
	
	@Autowired
	private CadastroVendaService cadastroVendaService;
	
	@Autowired
	private VendaValidator vendaValidator;
	
	@Autowired
	private Vendas vendas;
	
	@Autowired
	private Mailer mailer;
	
	@InitBinder("venda")
	public void inicializarValidador(WebDataBinder binder){
		binder.setValidator(vendaValidator);
	}
	
	@GetMapping("/nova")
	public ModelAndView nova(Venda venda) {
		ModelAndView mv = new ModelAndView("venda/CadastroVenda");
		
		setUuid(venda);
		
		mv.addObject("itens", venda.getItens());
		mv.addObject("valorFrete", venda.getValorFrete());
		mv.addObject("valorDesconto", venda.getValorDesconto());
		mv.addObject("valorTotalItens", tabelaItens.getValorTotal(venda.getUuid()));
		
		return mv;
	}
	
	@PostMapping(value = "/nova", params="salvar")
	public ModelAndView salvar(Venda venda, BindingResult resultado
				, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, resultado);
		if (resultado.hasErrors()){
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		
		cadastroVendaService.salvar(venda);
		attributes.addFlashAttribute("mensagem", "Venda salva com sucesso!");
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping(value = "/nova", params="emitir")
	public ModelAndView emitir(Venda venda, BindingResult resultado
				, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, resultado);
		if (resultado.hasErrors()){
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		
		cadastroVendaService.emitir(venda);
		attributes.addFlashAttribute("mensagem", "Venda emitida com sucesso!");
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping(value = "/nova", params="enviarEmail")
	public ModelAndView enviarEmail(Venda venda, BindingResult resultado
				, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		
		validarVenda(venda, resultado);
		if (resultado.hasErrors()){
			return nova(venda);
		}
		
		venda.setUsuario(usuarioSistema.getUsuario());
		
		
		venda = cadastroVendaService.salvar(venda);
		mailer.enviar(venda);
		/*System.out.println("#### Logo depois da chamada ao método enviar!");*/
		
		attributes.addFlashAttribute("mensagem", String.format("Venda nº %d salva com sucesso e e-mail enviado!", venda.getCodigo()));
		return new ModelAndView("redirect:/vendas/nova");
	}
	
	@PostMapping("/item")
	public ModelAndView adicionarItem(Long codigoCerveja, String uuid){
		Cerveja cerveja = cervejas.getOne(codigoCerveja);
		tabelaItens.adicionarItem(uuid, cerveja, 1);
		return mvTabelaItensVenda(uuid);
	}
	
	@PutMapping("/item/{codigoCerveja}")
	public ModelAndView alterarQuantidadeItem(@PathVariable("codigoCerveja") Cerveja cerveja
			, Integer quantidade, String uuid){
		tabelaItens.alterarQuantidadeItens(uuid, cerveja, quantidade);
		return mvTabelaItensVenda(uuid);
	}
	
	@DeleteMapping("/item/{uuid}/{codigoCerveja}")
	public ModelAndView excluirItem(@PathVariable("codigoCerveja") Cerveja cerveja
			, @PathVariable String uuid){
		tabelaItens.excluirItem(uuid, cerveja);
		return mvTabelaItensVenda(uuid);
	}
	
	@GetMapping
	public ModelAndView pesquisar(VendaFilter vendaFilter, BindingResult result
			, @PageableDefault(size = 8) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("venda/PesquisaVendas");
		mv.addObject("statusVenda", StatusVenda.values());
		mv.addObject("tiposPessoa", TipoPessoa.values());
		
		PageWrapper<Venda> pagina = new PageWrapper<>(vendas.filtrar(vendaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", pagina);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo){
		Venda venda = vendas.buscarComItens(codigo);
		
		setUuid(venda);
		for(ItemVenda item : venda.getItens()){
			tabelaItens.adicionarItem(venda.getUuid(), item.getCerveja(), item.getQuantidade());
		}
		
		ModelAndView mv = nova(venda);
		mv.addObject(venda);
		return mv;
	}
	
	@PostMapping(value = "/nova", params="cancelar")
	public ModelAndView cancelar(Venda venda, BindingResult resultado
				, RedirectAttributes attributes, @AuthenticationPrincipal UsuarioSistema usuarioSistema) {
		try {
			cadastroVendaService.cancelar(venda);
		} catch (AccessDeniedException e) {
			return new ModelAndView("/403");
		}
		attributes.addFlashAttribute("mensagem", "Venda cancelada com sucesso!");
		return new ModelAndView("redirect:/vendas/" + venda.getCodigo());
	}
	
	@GetMapping("/totalPorMes")
	public @ResponseBody List<VendaMes> listarTotalVendaPorMes(){
		return vendas.totalPorMes();
	}
	
	@GetMapping("/porOrigem")
	public @ResponseBody List<VendaOrigem> vendasPorNacionalidade() {
		return this.vendas.totalPorOrigem();
	}

	private ModelAndView mvTabelaItensVenda(String uuid) {
		ModelAndView mv = new ModelAndView("venda/TabelaItensVenda");
		mv.addObject("itens", tabelaItens.getItens(uuid));
		mv.addObject("valorTotal", tabelaItens.getValorTotal(uuid));
		return mv;
	}
	
	private void validarVenda(Venda venda, BindingResult resultado) {
		venda.adicionarItens(tabelaItens.getItens(venda.getUuid()));
		venda.calcularValorTotal();
		
		vendaValidator.validate(venda, resultado);
	}
	
	private void setUuid(Venda venda) {
		if(StringUtils.isEmpty(venda.getUuid())){
			venda.setUuid(UUID.randomUUID().toString());
		}
	}

}
