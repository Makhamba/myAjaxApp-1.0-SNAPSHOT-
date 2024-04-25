package com.ajax;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
/*import javax.swing.text.html.HTMLDocument.Iterator;*/

@WebServlet(name = "AutoCompleteServlet", urlPatterns = {"/autocomplet"})
public class AutoCompleteServlet extends HttpServlet {
private ServletContext context;
private ComposerData compData = new ComposerData();
private HashMap composers = compData.getComposers();
    

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();
    }

    @Override
    public String getServletName() {
        return super.getServletName(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void log(String message, Throwable t) {
        super.log(message, t); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void log(String msg) {
        super.log(msg); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void init() throws ServletException {
        super.init(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public ServletContext getServletContext() {
        return super.getServletContext(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public ServletConfig getServletConfig() {
        return super.getServletConfig(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public Enumeration<String> getInitParameterNames() {
        return super.getInitParameterNames(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public String getInitParameter(String name) {
        return super.getInitParameter(name); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

    @Override
    public void destroy() {
        super.destroy(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }
    
   @Override
public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    String action = request.getParameter("action");
    String targetId = request.getParameter("id");
    StringBuffer sb = new StringBuffer();

    if (targetId != null) {
        targetId = targetId.trim().toLowerCase();
    } else {
        context.getRequestDispatcher("/error.jsp").forward(request, response);
    }

    boolean namesAdded = false;
    if (action.equals("complete")) {

        // check if user sent empty string
        if (!targetId.equals("")) {

            Iterator it = (Iterator) composers.keySet().iterator();

            while (it.hasNext()) {
                String id = (String) it.next();
                Composer composer = (Composer) composers.get(id);

                if ( // targetId matches first name
                     composer.getFirstName().toLowerCase().startsWith(targetId) ||
                     // targetId matches last name
                     composer.getLastName().toLowerCase().startsWith(targetId) ||
                     // targetId matches full name
                     composer.getFirstName().toLowerCase().concat(" ")
                        .concat(composer.getLastName().toLowerCase()).startsWith(targetId)) {

                    sb.append("<composer>");
                    sb.append("<id>" + composer.getId() + "</id>");
                    sb.append("<firstName>" + composer.getFirstName() + "</firstName>");
                    sb.append("<lastName>" + composer.getLastName() + "</lastName>");
                    sb.append("</composer>");
                    namesAdded = true;
                }
            }
        }

        if (namesAdded) {
            response.setContentType("text/xml");
            response.setHeader("Cache-Control", "no-cache");
            response.getWriter().write("<composers>" + sb.toString() + "</composers>");
        } else {
            //nothing to show
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
    }
    if (action.equals("lookup")) {

        // put the target composer in the request scope to display
       if ((targetId != null) && composers.containsKey(targetId.trim())) {
            request.setAttribute("composer", composers.get(targetId));
            context.getRequestDispatcher("/composer.jsp").forward(request, response);
        }
    }
}
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
