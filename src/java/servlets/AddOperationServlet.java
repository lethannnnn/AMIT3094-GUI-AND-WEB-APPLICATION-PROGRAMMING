/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import da.CategoryDao;
import da.ProductDao;
import domain.Category;
import domain.Message;
import domain.Product;
import helper.ConnectionProvider;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpSession;
@WebServlet("/AddOperationServlet")
@MultipartConfig
public class AddOperationServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GetStudentDetails</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet GetStudentDetails at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
            
        }
    }
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
                String operation = request.getParameter("operation");
                HttpSession session = request.getSession();
                Message message = null;

                try {
                    CategoryDao catDao = new CategoryDao(ConnectionProvider.getConnection());
                    ProductDao pdao = new ProductDao(ConnectionProvider.getConnection());
                
		if (operation.trim().equals("addCategory")) {

			String categoryName = request.getParameter("category_name");
			Part part = request.getPart("category_img");
			Category category = new Category(categoryName, part.getSubmittedFileName());
			boolean flag = catDao.saveCategory(category);

			String path = request.getServletContext().getRealPath("/") + "tmp" + File.separator
					+ part.getSubmittedFileName();

			try {
				FileOutputStream fos = new FileOutputStream(path);
				InputStream is = part.getInputStream();
				byte[] data = new byte[is.available()];
				is.read(data);
				fos.write(data);
				fos.flush();
				fos.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			if (flag) {
				message = new Message("Category added successfully!!", "success", "alert-success");
			} else {
                                message = new Message("Something went wrong! Try again!!", "error", "alert-danger");
			}
			session.setAttribute("message", message);
			response.sendRedirect("admin.jsp");

		} else if (operation.trim().equals("addProduct")) {

			// add product to database
			String pName = request.getParameter("name");
			String pDesc = request.getParameter("description");
			float pPrice = Integer.parseInt(request.getParameter("price"));
			int pDiscount = Integer.parseInt(request.getParameter("discount"));
			if (pDiscount < 0 || pDiscount > 100) {
				pDiscount = 0;
			}
			int pQuantity = Integer.parseInt(request.getParameter("quantity"));
			Part part = request.getPart("photo");
			int categoryType = Integer.parseInt(request.getParameter("categoryType"));

			Product product = new Product(pName, pDesc, pPrice, pDiscount, pQuantity, part.getSubmittedFileName(),
					categoryType);
			boolean flag = pdao.saveProduct(product);

			String path = request.getServletContext().getRealPath("/") + "tmp" + File.separator
					+ part.getSubmittedFileName();
			try {
				FileOutputStream fos = new FileOutputStream(path);
				InputStream is = part.getInputStream();
				byte[] data = new byte[is.available()];
				is.read(data);
				fos.write(data);
				fos.flush();
				fos.close();

			} catch (Exception e) {
				e.printStackTrace();
                                String errorMessage = "NullPointerException occurred. Variables involved: ";
                                errorMessage += "pName=" + request.getParameter("name") + ", ";
                                errorMessage += "pDesc=" + request.getParameter("description") + ", ";
                                errorMessage += "pPrice=" + request.getParameter("price") + ", ";
                                errorMessage += "pDiscount=" + request.getParameter("discount") + ", ";
                                errorMessage += "pQuantity=" + request.getParameter("quantity") + ", ";
                                errorMessage += "photo=" + request.getPart("photo").getSubmittedFileName() + ", ";
                                errorMessage += "categoryType=" + request.getParameter("categoryType");
                                message = new Message(errorMessage, "error", "alert-danger");
                                session.setAttribute("message", message);
                                response.sendRedirect("admin.jsp");
			}
			if (flag) {
				message = new Message("Product added successfully!!", "success", "alert-success");
			} else {
				message = new Message("Something went wrong! Try again!!", "error", "alert-danger");
			}
			session.setAttribute("message", message);
			response.sendRedirect("admin.jsp");
			
		} else if (operation.trim().equals("updateCategory")) {

			int cid = Integer.parseInt(request.getParameter("cid"));
			String name = request.getParameter("category_name");
			Part part = request.getPart("category_img");
			if (part.getSubmittedFileName().isEmpty()) {
				String image = request.getParameter("image");
				Category category = new Category(cid, name, image);
				catDao.updateCategory(category);
			} else {
				Category category = new Category(cid, name, part.getSubmittedFileName());
				catDao.updateCategory(category);
				String path = request.getServletContext().getRealPath("/") + "tmp" + File.separator
						+ part.getSubmittedFileName();
				try {
					FileOutputStream fos = new FileOutputStream(path);
					InputStream is = part.getInputStream();
					byte[] data = new byte[is.available()];
					is.read(data);
					fos.write(data);
					fos.flush();
					fos.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			message = new Message("Category updated successfully!!", "success", "alert-success");
			session.setAttribute("message", message);
			response.sendRedirect("display_category.jsp");
			
		} else if (operation.trim().equals("deleteCategory")) {

			int cid = Integer.parseInt(request.getParameter("cid"));
			catDao.deleteCategory(cid);
			response.sendRedirect("display_category.jsp");

		} else if (operation.trim().equals("updateProduct")) {

			int pid = Integer.parseInt(request.getParameter("pid"));
			String name = request.getParameter("name");
			float price = Float.parseFloat(request.getParameter("price"));
			String description = request.getParameter("description");
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			int discount = Integer.parseInt(request.getParameter("discount"));
			if (discount < 0 || discount > 100) {
				discount = 0;
			}
			Part part = request.getPart("product_img");
			int cid = Integer.parseInt(request.getParameter("categoryType"));
			if (cid == 0) {
				cid = Integer.parseInt(request.getParameter("category"));
			}
			if (part.getSubmittedFileName().isEmpty()) {
				String image = request.getParameter("image");
				Product product = new Product(pid, name, description, price, discount, quantity, image, cid);
				pdao.updateProduct(product);
			} else {

				Product product = new Product(pid, name, description, price, discount, quantity,
						part.getSubmittedFileName(), cid);
				pdao.updateProduct(product);
				// product image upload
				String path = request.getServletContext().getRealPath("/") + "tmp" + File.separator
						+ part.getSubmittedFileName();
				try {
					FileOutputStream fos = new FileOutputStream(path);
					InputStream is = part.getInputStream();
					byte[] data = new byte[is.available()];
					is.read(data);
					fos.write(data);
					fos.flush();
					fos.close();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			message = new Message("Product updated successfully!!", "success", "alert-success");
			session.setAttribute("message", message);
			response.sendRedirect("display_products.jsp");

		} else if (operation.trim().equals("deleteProduct")) {

			int pid = Integer.parseInt(request.getParameter("pid"));
			pdao.deleteProduct(pid);
			response.sendRedirect("display_products.jsp");

		}
	
            } catch (NullPointerException e) {
                e.printStackTrace();
          
}
            return; 
            }
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}
}