package org.jcrypto.server;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jcrypto.JCryptoServiceUtil;
import org.jcrypto.model.NamesProvider;
import org.jcrypto.model.Response;
import org.jcrypto.operation.OperationFactory;

import java.io.IOException;
import java.util.Map;

public class JCryptoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String opId = req.getParameter("op_id");
            String payload = req.getParameter("payload");
            Map<String, String> stringStringMap = NamesProvider.parseInputs(payload);
            Object result = OperationFactory.prepareOperation(opId, stringStringMap).process();
            String responsePayload = JCryptoServiceUtil.serialize(result);
            resp.getWriter().write(responsePayload);
        }
        catch (Exception ex) {
            resp.setStatus(500);
            String errorPayload = JCryptoServiceUtil.serialize(Response.errorResponse(ex));
            resp.getWriter().write(errorPayload);
        }
    }
}
