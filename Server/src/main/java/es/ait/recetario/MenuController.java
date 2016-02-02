/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.ait.recetario;

import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author aitkiar
 */
@Controller
public class MenuController
{

    @RequestMapping("menu")
    public String menu( HttpServletRequest request, Model model )
    {
        String uri = request.getRequestURI();
        System.out.println( uri );
        switch ( uri )
        {
            case "/recipe/search":
                model.addAttribute("searchClass", " class='menuSelected'");
                break;
            case "/recetario/recipe/new.jsp":
                model.addAttribute("newClass", " class='menuSelected'");
                break;
            case "/recipe/synchronize":
                model.addAttribute("synchronizeClass", " class='menuSelected'");
                break;
            case "/recetario/config/parameters.jsp":
                model.addAttribute("configClass", " class='menuSelected'");
                break;
            default:
                model.addAttribute("searchClass", " class='menuSelected'");
                break;
        }
        return "menu.jsp";
    }
}
