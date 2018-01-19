package com.tjgo.brauer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tjgo.brauer.controller.page.PageWrapper;
import com.tjgo.brauer.dto.CervejaDTO;
import com.tjgo.brauer.model.Cerveja;
import com.tjgo.brauer.model.Origem;
import com.tjgo.brauer.model.Sabor;
import com.tjgo.brauer.repository.Cervejas;
import com.tjgo.brauer.repository.Estilos;
import com.tjgo.brauer.repository.filter.CervejaFilter;
import com.tjgo.brauer.service.CadastroCervejaService;
import com.tjgo.brauer.service.exception.ImpossivelExcluirEntidadeException;

@Controller
@RequestMapping("/cervejas")
public class CervejasController {
	
	@Autowired
	private Estilos estilos;
	
	@Autowired
	private CadastroCervejaService cadastroCervejaService;
	
	@Autowired
	private Cervejas cervejas;
	
	@RequestMapping("/nova")
	public ModelAndView nova(Cerveja cerveja){
		
		ModelAndView mv = new ModelAndView("cerveja/CadastroCerveja");
		mv.addObject("sabores", Sabor.values());
		mv.addObject("estilos", estilos.findAll());
		mv.addObject("origens", Origem.values());
		/*
		Optional<Cerveja> cervejaOptional = cervejas.findBySkuIgnoreCase("AA1111");
		System.out.println(cervejaOptional.isPresent());
		*/
		
		/*logger.error("Aqui é um log nível error!");
		logger.info("Aqui é um log nível info!");
		
		if (logger.isDebugEnabled()){
			logger.debug("Aqui é um log nível debug!");
			logger.debug("O objeto cerveja eh: "+cerveja);
		}*/
		
		return mv;
	}
	
	@RequestMapping(value={"/nova", "{\\d+}"}, method=RequestMethod.POST)
	public ModelAndView salvar(@Valid Cerveja cerveja, BindingResult resultado, Model model, RedirectAttributes atributos){
		if (resultado.hasErrors()){
			//throw new RuntimeException(); //Simular o erro 500
			return nova(cerveja); //1 chamada ao html;
		} 
/*		
 * 		System.out.println(">>>>>sku: "+cerveja.getSku()+"- nome: "+cerveja.getNome()+" - descricao: "+cerveja.getDescricao()+" <<<<<");
 *		System.out.println(">>>>>sabor: "+cerveja.getSabor()+"<<<<<");
*/		
		cadastroCervejaService.salvar(cerveja);
		atributos.addFlashAttribute("mensagem", "Cerveja salva com sucesso!");
		return new ModelAndView("redirect:/cervejas/nova"); //2 chamadas, a 2ª chama url;
	}
	
	@GetMapping
	public ModelAndView pesquisar(CervejaFilter cervejaFilter, BindingResult result
			, @PageableDefault(size = 4) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cerveja/PesquisaCervejas");
		mv.addObject("estilos", estilos.findAll());
		mv.addObject("sabores", Sabor.values());
		mv.addObject("origens", Origem.values());
		
		PageWrapper<Cerveja> pagina = new PageWrapper<>(cervejas.filtrar(cervejaFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", pagina);
		return mv;
	}
	
	@RequestMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<CervejaDTO> pesquisar(String skuOuNome){
		return cervejas.porSkuOuNome(skuOuNome);
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Cerveja cerveja){
		try {
			cadastroCervejaService.excluir(cerveja);
		} catch (ImpossivelExcluirEntidadeException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar (@PathVariable("codigo") Cerveja cerveja){
		ModelAndView mv = nova(cerveja);
		mv.addObject(cerveja);
		return mv;
	}
	
}
