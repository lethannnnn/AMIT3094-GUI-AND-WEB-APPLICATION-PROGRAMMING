package servlets;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import da.UserDao;
import domain.Message;
import domain.User;
import helper.ConnectionProvider;
import javax.servlet.http.HttpSession;
/**
 *
 * @author Ethan
 */
public class UpdateUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String op = request.getParameter("operation");
		HttpSession session = request.getSession();
		User oldUser = (User) session.getAttribute("activeUser");
		UserDao userDao = new UserDao(ConnectionProvider.getConnection());

		if (op.trim().equals("changeAddress")) {
			try {
				String userAddress = request.getParameter("user_address");
				String userCity = request.getParameter("city");
				String userPincode = request.getParameter("pincode");
				String userState = request.getParameter("state");

				User user = new User();
				user.setUserId(oldUser.getUserId());
				user.setUserName(oldUser.getUserName());
				user.setUserEmail(oldUser.getUserEmail());
				user.setUserPassword(oldUser.getUserPassword());
				user.setUserPhone(oldUser.getUserPhone());
				user.setUserGender(oldUser.getUserGender());
				user.setDateTime(oldUser.getDateTime());
				user.setUserAddress(userAddress);
				user.setUserCity(userCity);
				user.setUserPincode(userPincode);
				user.setUserState(userState);

				userDao.updateUserAddress(user);
				session.setAttribute("activeUser", user);
				response.sendRedirect("checkout.jsp");

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (op.trim().equals("updateUser")) {
    try {
        // Retrieve existing user from session
        User user = (User) session.getAttribute("activeUser");
        if (user == null) {
            // Handle error: user not found in session
            return;
        }

        // Get parameters from the request
        String userPhone = request.getParameter("mobile_no");
        String userGender = request.getParameter("gender");
        String userAddress = request.getParameter("address");
        String userCity = request.getParameter("city");
        String userPincode = request.getParameter("pincode");
        String userState = request.getParameter("state");

        // Update only the fields that are allowed to be updated
        user.setUserPhone(userPhone);
        user.setUserGender(userGender);
        user.setUserAddress(userAddress);
        user.setUserCity(userCity);
        user.setUserPincode(userPincode);
        user.setUserState(userState);

        // Update the user in the database
        userDao.updateUser(user);

        // Set a success message and redirect
        Message message = new Message("User information updated successfully!!", "success", "alert-success");
        session.setAttribute("message", message);
        response.sendRedirect("profile.jsp");

    } catch (Exception e) {
        e.printStackTrace();
        // Handle exception
    }


		} else if (op.trim().equals("deleteUser")) {
			int uid = Integer.parseInt(request.getParameter("uid"));
			userDao.deleteUser(uid);
			response.sendRedirect("display_users.jsp");
		}
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);
	}

}