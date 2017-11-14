package jaom.org;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Servlet implementation class DBClassic
 */
@WebServlet("/jdbc_classic")
public class DBClassic extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DBClassic() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection con= null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		try {
			PrintWriter out = response.getWriter();
			Class.forName("org.postgresql.Driver");
			con= DriverManager.getConnection("jdbc:postgresql://localhost:5432/Test","postgres","23741340");
			JSONObject json = new JSONObject();
			JSONArray table = new JSONArray();
			JSONObject row;
			String query;
			if(request.getParameter("ced").trim().equals("")) {
				query ="SELECT * FROM TEST";
			}
			else {
				query = "SELECT * FROM TEST WHERE ced = ?";
			}
			pstm = con.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			if(!request.getParameter("ced").trim().equals("")) {
				pstm.setInt(1, Integer.parseInt(request.getParameter("ced")));
			}
			rs = pstm.executeQuery();
			rsmd = rs.getMetaData();
			rs.beforeFirst();
			while (rs.next()) {
				row = new JSONObject();
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {
					row.put(rsmd.getColumnLabel(i), rs.getObject(i));
				}
				table.put(row);
			}
			json.put("query", table);
			out.print(json.toString());
		}
		catch(SQLException e){
			e.getStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			try {
				rs.close();
				pstm.close();
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				System.out.println(e);
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
