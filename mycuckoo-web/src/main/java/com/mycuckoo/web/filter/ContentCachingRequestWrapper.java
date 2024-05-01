package com.mycuckoo.web.filter;

import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.util.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author rutine
 * @time May 1, 2024 8:55:26 AM
 */
public class ContentCachingRequestWrapper extends HttpServletRequestWrapper {

    private static final String FORM_CONTENT_TYPE = "application/x-www-form-urlencoded";


    private final ByteArrayOutputStream cachedContent;

    @Nullable
    private final Integer contentCacheLimit;

    @Nullable
    private ServletInputStream inputStream;

    @Nullable
    private BufferedReader reader;


    /**
     * Create a new ContentCachingRequestWrapper for the given servlet request.
     * @param request the original servlet request
     */
    public ContentCachingRequestWrapper(HttpServletRequest request) {
        super(request);
        int contentLength = request.getContentLength();
        this.cachedContent = new ByteArrayOutputStream(contentLength >= 0 ? contentLength : 1024);
        this.contentCacheLimit = null;
    }

    /**
     * Create a new ContentCachingRequestWrapper for the given servlet request.
     * @param request the original servlet request
     * @param contentCacheLimit the maximum number of bytes to cache per request
     * @since 4.3.6
     */
    public ContentCachingRequestWrapper(HttpServletRequest request, int contentCacheLimit) {
        super(request);
        this.cachedContent = new ByteArrayOutputStream(contentCacheLimit);
        this.contentCacheLimit = contentCacheLimit;
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        if (this.inputStream == null) {
            this.inputStream = new ContentCachingInputStream(getRequest().getInputStream());
        }
        return this.inputStream;
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        if (this.reader == null) {
            this.reader = new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
        }
        return this.reader;
    }

    @Override
    public String getParameter(String name) {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameter(name);
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterMap();
    }

    @Override
    public Enumeration<String> getParameterNames() {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterNames();
    }

    @Override
    public String[] getParameterValues(String name) {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        return super.getParameterValues(name);
    }


    private boolean isFormPost() {
        String contentType = getContentType();
        return (contentType != null && contentType.contains(FORM_CONTENT_TYPE) &&
                HttpMethod.POST.matches(getMethod()));
    }

    private void writeRequestParametersToCachedContent() {
        try {
            if (this.cachedContent.size() == 0) {
                String requestEncoding = getCharacterEncoding();
                Map<String, String[]> form = super.getParameterMap();
                for (Iterator<String> nameIterator = form.keySet().iterator(); nameIterator.hasNext(); ) {
                    String name = nameIterator.next();
                    List<String> values = Arrays.asList(form.get(name));
                    for (Iterator<String> valueIterator = values.iterator(); valueIterator.hasNext(); ) {
                        String value = valueIterator.next();
                        this.cachedContent.write(URLEncoder.encode(name, requestEncoding).getBytes());
                        if (value != null) {
                            this.cachedContent.write('=');
                            this.cachedContent.write(URLEncoder.encode(value, requestEncoding).getBytes());
                            if (valueIterator.hasNext()) {
                                this.cachedContent.write('&');
                            }
                        }
                    }
                    if (nameIterator.hasNext()) {
                        this.cachedContent.write('&');
                    }
                }
            }
        }
        catch (IOException ex) {
            throw new IllegalStateException("Failed to write request parameters to cached content", ex);
        }
    }

    /**
     * Return the cached request content as a byte array.
     * <p>The returned array will never be larger than the content cache limit.
     * @see #ContentCachingRequestWrapper(HttpServletRequest, int)
     */
    public byte[] getContentAsByteArray() {
        if (this.cachedContent.size() == 0 && isFormPost()) {
            writeRequestParametersToCachedContent();
        }
        else if (cachedContent.size() == 0) {
            try {
                initContentCaching();
            } catch (IOException e) {

            }
        }

        return this.cachedContent.toByteArray();
    }

    private void initContentCaching() throws IOException {
        while (contentCacheLimit == null || cachedContent.size() < contentCacheLimit) {
            int ch = getRequest().getInputStream().read();
            if (ch == -1) {
                break;
            }

            cachedContent.write(ch);
        }
    }


    private class ContentCachingInputStream extends ServletInputStream {
        private final ServletInputStream is;
        private byte[] cachedContent = new byte[0];
        private int pos = -1;


        public ContentCachingInputStream(ServletInputStream is) {
            this.is = is;
        }

        @Override
        public int read() throws IOException {
            if (cachedContent.length == 0 && ContentCachingRequestWrapper.this.cachedContent.size() > 0) {
                cachedContent = ContentCachingRequestWrapper.this.getContentAsByteArray();
            }
            if (cachedContent.length > 0) {
                if (pos < (cachedContent.length - 1)) {
                    return cachedContent[++pos] & 0xFF;
                }
            }

            return this.is.read();
        }

        @Override
        public boolean isFinished() {
            return this.is.isFinished();
        }

        @Override
        public boolean isReady() {
            return this.is.isReady();
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            this.is.setReadListener(readListener);
        }
    }
}
