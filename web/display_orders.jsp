<%@page import="domain.Admin"%>
<%@page import="domain.Message"%>
<%@page import="da.UserDao"%>
<%@page errorPage="error_exception.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="domain.OrderedProduct"%>
<%@page import="domain.Order"%>
<%@page import="java.util.List"%>
<%@page import="da.OrderedProductDao"%>
<%@page import="da.OrderDao"%>
<%@page import="helper.ConnectionProvider"%>

<%
Admin activeAdmin = (Admin) session.getAttribute("activeAdmin");
if (activeAdmin == null) {
	Message message = new Message("You are not logged in! Login first!!", "error", "alert-danger");
	session.setAttribute("message", message);
	response.sendRedirect("adminLogin.jsp");
	return;
}
OrderDao orderDao = new OrderDao(ConnectionProvider.getConnection());
OrderedProductDao ordProdDao = new OrderedProductDao(ConnectionProvider.getConnection());
List<Order> orderList = orderDao.getAllOrder();
UserDao userDao = new UserDao(ConnectionProvider.getConnection());
%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>View Order's</title>
<%@include file="Components/common_css_js.jsp"%>
</head>
<body>
	<!--navbar -->
	<%@include file="Components/navbar.jsp"%>

	<!-- order details -->

	<div class="container-fluid px-3 py-3">
		<%
		if (orderList == null || orderList.size() == 0) {
		%>
		<div class="container mt-5 mb-5 text-center">
			<img src="Images/empty-cart.png" style="max-width: 200px;"
				class="img-fluid">
			<h4 class="mt-3">Zero Order found</h4>
		</div>
		<%
		} else {
		%>
                <%@include file="Components/alert_message.jsp" %>
                <table class="table table-hover">
				<tr class="table-primary" style="font-size: 18px;">
					<th class="text-center">Product</th>
					<th>Order ID</th>
					<th>Product Details</th>
					<th>Delivery Address</th>
					<th>Date & Time</th>
					<th>Payment Type</th>
					<th>Status</th>
					<th colspan="2" class="text-center">Action</th>
				</tr>
		<div class="container-fluid">
                    <%
				for (Order order : orderList) {
					List<OrderedProduct> ordProdList = ordProdDao.getAllOrderedProduct(order.getId());
					for (OrderedProduct orderProduct : ordProdList) {
				%>
                    <form action="UpdateOrderServlet" method="post">
                        <input type="hidden" name="orderid" value="<%=order.getOrderId()%>">	

				<tr>
					<td class="text-center"><img
						src="tmp/<%=orderProduct.getImage()%>"
						style="width: 50px; height: 50px; width: auto;"></td>
					<td><%=order.getOrderId()%></td>
					<td><%=orderProduct.getName()%><br>Quantity: <%=orderProduct.getQuantity()%><br>Total
						Price: &#36;<%=orderProduct.getPrice() * orderProduct.getQuantity()%></td>
					<td><%=userDao.getUserName(order.getUserId())%><br>Mobile No. <%=userDao.getUserPhone(order.getUserId())%><br><%=userDao.getUserAddress(order.getUserId())%></td>
					<td><%=order.getDate()%></td>
					<td><%=order.getPaymentType()%></td>
					<td><%=order.getStatus()%></td>
					<td>
    <select id="operation" name="status" class="form-select">
            <%
                String currentStatus = order.getStatus();
                // Use a scriptlet to conditionally select the appropriate option
                if (currentStatus.equals("Order Confirmed")) {
            %>
                <option value="Order Confirmed" selected>Order Confirmed</option>
            <%
                } else {
            %>
                <option value="Order Confirmed">Order Confirmed</option>
            <%
                }
                if (currentStatus.equals("Shipped")) {
            %>
                <option value="Shipped" selected>Shipped</option>
            <%
                } else {
            %>
                <option value="Shipped">Shipped</option>
            <%
                }
                if (currentStatus.equals("Out For Delivery")) {
            %>
                <option value="Out For Delivery" selected>Out For Delivery</option>
            <%
                } else {
            %>
                <option value="Out For Delivery">Out For Delivery</option>
            <%
                }
                if (currentStatus.equals("Delivered")) {
            %>
                <option value="Delivered" selected>Delivered</option>
            <%
                } else {
            %>
                <option value="Delivered">Delivered</option>
            <%
                }
            %>
        </select>
    </td>
					<td>
						<%
						if (order.getStatus().equals("Delivered")) {
						%>
						<button type="submit" class="btn btn-success disabled">Update</button>
						<%
						} else {
						%>
						<button type="submit" class="btn btn-secondary">Update</button> 
						<%
						 }
						 %>
					</td>
				</tr>
                           
				<%
				}
				}
				%>
			</table>
                    
		</div>
                
		<%
		}
		%>
                </form>
	</div>
</body>
</html>