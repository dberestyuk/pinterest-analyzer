package com.lue.pintrests;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lue.board.Board;
import com.lue.pins.PinData;

public class Analyzeservlet extends HttpServlet {
	private static final long serialVersionUID = -5975655570646523310L;

	private static final Logger log = LoggerFactory
			.getLogger(Analyzeservlet.class);

	public static int numberOfPinsToAnalyze;
	public static int offsetNumberOfPins;
	public static String filterkeyword;

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		log.info(request.getParameterMap().toString());
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		HttpSession session = request.getSession();
		try {
			String boardName = request.getParameter("url");
			numberOfPinsToAnalyze = Integer.parseInt(request
					.getParameter("nopins"));
			offsetNumberOfPins = Integer.parseInt(request
					.getParameter("offset"));
			filterkeyword = request.getParameter("filterwords");
			
			Board board = PinterestAnalyzer.getBoard(boardName);
			List<PinData> boardPins = PinterestAnalyzer.getPinsForBoard(board.getId());

			Map<String, FinalResult> results = PinterestAnalyzer.analyzePins(boardPins);
			
			log.info("Finished processing, sending response for {} pins", results.size());
			session.setAttribute("value", results);

			response.sendRedirect("pinDetails.jsp");
		} catch (Throwable e) {
			log.error(e.getMessage(), e);
		} finally {
			out.close();
		}
	}

	/**
	 * Handles the HTTP <code>GET</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 *
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 *
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}
}
