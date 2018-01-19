package com.tjgo.brauer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.tjgo.brauer.controller.page.PageWrapper;
import com.tjgo.brauer.model.Usuario;
import com.tjgo.brauer.repository.Grupos;
import com.tjgo.brauer.repository.Usuarios;
import com.tjgo.brauer.repository.filter.UsuarioFilter;
import com.tjgo.brauer.service.CadastroUsuarioService;
import com.tjgo.brauer.service.StatusUsuario;
import com.tjgo.brauer.service.exception.EmailUsuarioJaCadastradoException;
import com.tjgo.brauer.service.exception.SenhaObrigatoriaUsuarioException;


@Controller
@RequestMapping("/usuarios")
public class UsuariosController {
	
	@Autowired
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Autowired
	private Grupos grupos;
	
	@Autowired
	private Usuarios usuarios;

	@RequestMapping("/novo")
	public ModelAndView novo(Usuario usuario){
		ModelAndView mv = new ModelAndView( "usuario/CadastroUsuario");
		mv.addObject("grupos", grupos.findAll());
		return mv;
	}
	
	@RequestMapping(value={"/novo" , "{\\+d}"}, method=RequestMethod.POST)
	public ModelAndView salvar(@Valid Usuario usuario, BindingResult resultado,  RedirectAttributes atributos){
		if (resultado.hasErrors()){
			return novo(usuario); //1 chamada ao html;
		} 
		
		try {
			cadastroUsuarioService.salvar(usuario);
		} catch (EmailUsuarioJaCadastradoException e) {
			resultado.rejectValue("email", e.getMessage(), e.getMessage());
			return novo(usuario);
		} catch (SenhaObrigatoriaUsuarioException e) {
			resultado.rejectValue("senha", e.getMessage(), e.getMessage());
			return novo(usuario);
		}
		atributos.addFlashAttribute("mensagem", "Usuario salvo com sucesso!");
		return new ModelAndView("redirect:/usuarios/novo"); //2 chamadas, a 2ª chama url;
	}
	
	@GetMapping
	public ModelAndView pesquisar(UsuarioFilter usuarioFilter, BindingResult result
			, @PageableDefault(size = 4) Pageable pageable, HttpServletRequest httpServletRequest) {
		ModelAndView mv = new ModelAndView("usuario/PesquisaUsuarios");
		mv.addObject("grupos", grupos.findAll());
		mv.addObject("usuarios", usuarios.findAll());
		
		PageWrapper<Usuario> pagina = new PageWrapper<>(usuarios.filtrar(usuarioFilter, pageable)
				, httpServletRequest);
		mv.addObject("pagina", pagina);
		return mv;
	}
	
	@PutMapping("/status")
	@ResponseStatus(HttpStatus.OK)
	public void atualizarStatus(@RequestParam ("codigos[]") Long[] codigos, @RequestParam("status") StatusUsuario statusUsuario){
		/*Arrays.asList(codigos).forEach(System.out::println);
		System.out.println(">>>>Status: "+status);*/
		cadastroUsuarioService.alterarStatus (codigos, statusUsuario);
	}
	
	@GetMapping("/{codigo}")
	public ModelAndView editar(@PathVariable Long codigo){
		Usuario usuario = usuarios.buscarComGrupos(codigo);
		ModelAndView mv = novo(usuario);
		mv.addObject(usuario);
		return mv;
	}
	
}
