package majiang.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

import majiang.dto.Result;

/**
 * Controller的基类
 * </pre>
 *
 * @see
 */
public class BaseController {

    /**
     * 保存用户对象到Session中
     *
     * @param request
     * @param user
     */

    public Result result(int status, Object data, String msg) {
        return new Result(status, data, msg);
    }

    public Result result(int status, Object data, String msg, Object extra) {
        return new Result(status, data, msg, extra);
    }

    public int getRequestParamsInt(HttpServletRequest req, String paramsName, int defaultVal) {
        int intparam = defaultVal;
        String val = req.getParameter(paramsName);
        if (StringUtils.isBlank(val) || val.equals("null"))
            return intparam;
        try {
            intparam = Integer.parseInt(val);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultVal;
        }
        return intparam;

    }

    public String getRequestParamstr(HttpServletRequest req, String paramsName) {
        String val = "";
        try {
            val = StringUtils.isNotBlank(req.getParameter(paramsName)) ? req.getParameter(paramsName) : "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return val;

    }
}
