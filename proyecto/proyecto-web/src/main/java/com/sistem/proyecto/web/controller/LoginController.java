/**
 * 
 */
package com.sistem.proyecto.web.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


/**
 * Controller para el Login. Implementación de mappings, servicios REST
 * y métodos de mapeo privados y públicos.
 * 
 */
@Controller
public class LoginController {
		
        @RequestMapping(value ="/login" , method = RequestMethod.GET)
	public ModelAndView welcomePage() {

		ModelAndView model = new ModelAndView();
		model.addObject("title", "Spring Security Hello World");
		model.addObject("message", "This is welcome page!");
		model.setViewName("login");
		return model;

	}

	
	
	/**
	 * Mapping para el metodo GET de la vista clientesLista.
	 * 
	 */
	@RequestMapping(value = {"/","home"}, method = RequestMethod.GET)
	public ModelAndView obtenerMisDatos(Model model) {
		return new ModelAndView("home");
	}

 
    

	
}
