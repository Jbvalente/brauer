package com.tjgo.brauer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tjgo.brauer.controller.page.PageWrapper;
import com.tjgo.brauer.model.Estilo;
import com.tjgo.brauer.repository.Estilos;
import com.tjgo.brauer.repository.filter.EstiloFilter;
import com.tjgo.brauer.service.CadastroEstiloService;
import com.tjgo.brauer.service.exception.EstiloUtilizadoNaCervejaException;
import com.tjgo.brauer.service.exception.NomeEstiloJaCadastradoException;

@Controller
@RequestMapping("/estilos")
public class EstilosController {
	
	@Autowired
	private CadastroEstiloService cadastroEstiloService;
	
	@Autowired
	private Estilos estilos;

	@RequestMapping("/novo")
	public ModelAndView novo(Estilo estilo){
		
		ModelAndView mv = new ModelAndView( "estilo/CadastroEstilo");
		return mv;
	}
	
	@RequestMapping(value={ "/novo", "{\\d+}"}, method=RequestMethod.POST)
	public ModelAndView cadastrar(@Valid Estilo estilo, BindingResult resultado,  RedirectAttributes atributos){
		if (resultado.hasErrors()){
			return novo(estilo); //1 chamada ao html;
		} 
	
		try{
			cadastroEstiloService.salvar(estilo);
		} catch (NomeEstiloJaCadastradoException e){
			resultado.rejectValue("nome", e.getMessage(), e.getMessage());
			return novo(estilo);
		}
		
		atributos.addFlashAttribute("mensagem", "Estilo salvo com sucesso!");
		return new ModelAndView("redirect:/estilos/novo"); //2 chamadas, a 2ª chama url;
	}
	
	@RequestMapping( method=RequestMethod.POST, consumes={ MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody ResponseEntity<?> salvar(@RequestBody  @Valid Estilo estilo, BindingResult resultado){
		
		if (resultado.hasErrors()){
			return ResponseEntity.badRequest().body(resultado.getFieldError("nome").getDefaultMessage());
		}
		
		//try{
			estilo = cadastroEstiloService.salvar(estilo);
		/*} catch (NomeEstiloJaCadastradoException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}*/
		
		return ResponseEntity.ok(estilo);
	}
	
	@GetMapping
	public ModelAndView pesquisar(EstiloFilter estiloFilter, BindingResult resultado
			, @PageableDefault(size = 5) Pageable pageable, HttpServletRequest httpServletRequest){
		ModelAndView mv= new ModelAndView("estilo/PesquisaEstilos");
		/*mv.addObject("estilos", estilos.findAll());*/
		
		PageWrapper<Estilo> pagina = new PageWrapper<>(estilos.filtrar(estiloFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", pagina);
		return mv;
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable("codigo") Long codigo) {
		Estilo estilo = estilos.getOne(codigo);
		ModelAndView mv = novo(estilo);
		mv.addObject(estilo);
		return mv;
	}
	
	@DeleteMapping("/{codigo}")
	public @ResponseBody ResponseEntity<?> excluir(@PathVariable("codigo") Estilo estilo) {
		try {
			cadastroEstiloService.excluir(estilo);
		} catch (EstiloUtilizadoNaCervejaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		return ResponseEntity.ok().build();
	}
	
}
