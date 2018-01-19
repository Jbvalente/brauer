package com.tjgo.brauer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tjgo.brauer.controller.page.PageWrapper;
import com.tjgo.brauer.model.Cidade;
import com.tjgo.brauer.repository.Cidades;
import com.tjgo.brauer.repository.Estados;
import com.tjgo.brauer.repository.filter.CidadeFilter;
import com.tjgo.brauer.service.CadastroCidadeService;
import com.tjgo.brauer.service.exception.CidadeUtilizadaPeloClienteException;
import com.tjgo.brauer.service.exception.NomeCidadeJaCadastradaException;

@Controller
@RequestMapping("/cidades")
public class CidadesController {
	
	@Autowired
	private Cidades cidades;
	
	@Autowired
	private Estados estados;
	
	@Autowired
	private CadastroCidadeService cadastroCidadeService;

	@RequestMapping("/nova")
	public ModelAndView nova(Cidade cidade){
		ModelAndView mv = new ModelAndView( "cidade/CadastroCidade");
		mv.addObject("estados", estados.findAll());
		return mv;
	}
	
	@Cacheable(value = "cidades", key = "#codigoEstado")
	@RequestMapping(consumes=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List <Cidade> pesquisarPorCodigoEstado(
				@RequestParam (name = "estado", defaultValue="-1") Long codigoEstado){
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) { }
		return cidades.findByEstadoCodigo(codigoEstado);
	}
	
	@RequestMapping(value={ "/nova", "{\\d+}"}, method=RequestMethod.POST)
	@CacheEvict(value = "cidades", key = "#cidade.estado.codigo", condition = "#cidade.temEstado()" )
	public ModelAndView salvar(@Valid Cidade cidade, BindingResult resultado,  RedirectAttributes atributos){
		if (resultado.hasErrors()){
			return nova(cidade); //1 chamada ao html;
		} 
		
		try {
			cadastroCidadeService.salvar(cidade);
		} catch (NomeCidadeJaCadastradaException e) {
			resultado.rejectValue("nome", e.getMessage(), e.getMessage());
			return nova(cidade);
		}
		atributos.addFlashAttribute("mensagem", "Cidade salva com sucesso!");
		return new ModelAndView("redirect:/cidades/nova"); //2 chamadas, a 2ª chama url;
	}
	
	@GetMapping
	public ModelAndView pesquisar(CidadeFilter cidadeFilter, BindingResult result
			, @PageableDefault(size = 11) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("cidade/PesquisaCidades");
		mv.addObject("estados", estados.findAll());
		
		PageWrapper<Cidade> pagina = new PageWrapper<>(cidades.filtrar(cidadeFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", pagina);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Long codigo) {
		Cidade cidade = cidades.buscarComEstado(codigo);
		ModelAndView mv = nova(cidade);
		mv.addObject(cidade);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Cidade cidade) {
		try {
			cadastroCidadeService.excluir(cidade);
		} catch (CidadeUtilizadaPeloClienteException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
}
