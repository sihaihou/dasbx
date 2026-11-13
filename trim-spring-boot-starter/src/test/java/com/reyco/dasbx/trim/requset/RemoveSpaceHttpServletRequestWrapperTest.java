package com.reyco.dasbx.trim.requset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.StreamUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class RemoveSpaceHttpServletRequestWrapperTest {

    @Test
    public void trimsAllParameterValuesWithoutChangingOriginalRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "  abc  ");
        request.addParameter("tag", " a ", " b ");

        RemoveSpaceHttpServletRequestWrapper wrapper =
                new RemoveSpaceHttpServletRequestWrapper(request);

        assertEquals("abc", wrapper.getParameter("name"));
        assertEquals("a", wrapper.getParameterValues("tag")[0]);
        assertEquals("b", wrapper.getParameterValues("tag")[1]);

        Map<String, String[]> parameterMap = wrapper.getParameterMap();
        assertEquals("abc", parameterMap.get("name")[0]);
        assertEquals("a", parameterMap.get("tag")[0]);
        assertEquals("b", parameterMap.get("tag")[1]);

        assertEquals("  abc  ", request.getParameter("name"));
    }

    @Test
    public void returnsCopiesInsteadOfInternalParameterArrays() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter("name", "  abc  ");

        RemoveSpaceHttpServletRequestWrapper wrapper =
                new RemoveSpaceHttpServletRequestWrapper(request);

        String[] values = wrapper.getParameterValues("name");
        values[0] = "changed";

        assertEquals("abc", wrapper.getParameter("name"));
    }

    @Test
    public void trimsJsonObjectAndArrayStrings() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json;charset=UTF-8");
        request.setContent((
                "{"
                + "\"name\":\" 张三 \","
                + "\"age\":18,"
                + "\"enabled\":true,"
                + "\"tags\":["
                + "\" a \","
                + "{\"city\":\" 北京 \"},"
                + "\" {\\\"name\\\":\\\"  c  \\\"} \""
                + "]"
                + "}").getBytes(StandardCharsets.UTF_8));

        RemoveSpaceHttpServletRequestWrapper wrapper =
                new RemoveSpaceHttpServletRequestWrapper(request);

        String result = StreamUtils.copyToString(
                wrapper.getInputStream(),
                StandardCharsets.UTF_8);

        JSONObject json = JSON.parseObject(result);
        assertEquals("张三", json.getString("name"));
        assertEquals(Integer.valueOf(18), json.getInteger("age"));
        assertTrue(json.getBooleanValue("enabled"));

        JSONArray tags = json.getJSONArray("tags");
        assertEquals("a", tags.getString(0));
        assertEquals("北京", tags.getJSONObject(1).getString("city"));
        assertEquals("{\"name\":\"  c  \"}", tags.getString(2));
    }

    @Test
    public void preservesJsonPrimitiveTypes() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setContent(" 123 ".getBytes(StandardCharsets.UTF_8));

        RemoveSpaceHttpServletRequestWrapper wrapper =
                new RemoveSpaceHttpServletRequestWrapper(request);

        String result = StreamUtils.copyToString(
                wrapper.getInputStream(),
                StandardCharsets.UTF_8);

        assertEquals("123", result);
        assertTrue(JSON.parse(result) instanceof Number);
    }

    @Test
    public void preservesInvalidJson() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setContentType("application/json");
        request.setContent("{invalid"
                .getBytes(StandardCharsets.UTF_8));

        RemoveSpaceHttpServletRequestWrapper wrapper =
                new RemoveSpaceHttpServletRequestWrapper(request);

        String result = StreamUtils.copyToString(
                wrapper.getInputStream(),
                StandardCharsets.UTF_8);

        assertEquals("{invalid", result);
        assertFalse(wrapper.getTrimBody().isEmpty());
    }
}