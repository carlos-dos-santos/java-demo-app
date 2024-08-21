package com.example.rest;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import static org.springframework.web.util.HtmlUtils.htmlEscape;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/messages")
public final class MessagesController {
    private static final List<String> lines = List.of(
            "Lorem ipsum dolor sit amet,",
            "consectetur adipiscing elit,",
            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");


    @RequestMapping(value = "html", method = RequestMethod.GET, produces = "text/html")
    public void getHtml(final HttpServletRequest request, final ServletResponse response)
            throws IOException, InterruptedException {
        render(Renderers.HTML, request, response);
    }

    @RequestMapping(value = "text", method = RequestMethod.GET, produces = "text/plain")
    public void getText(final HttpServletRequest request, final ServletResponse response)
            throws IOException, InterruptedException {
        render(Renderers.PLAIN, request, response);
    }

    private static void render(final IRenderer renderer, final HttpServletRequest request, final ServletResponse response)
            throws IOException, InterruptedException {
        final int stubLength = getStubLength(request);
        final ServletOutputStream outputStream = response.getOutputStream();
        renderer.renderStub(stubLength, outputStream);
        renderInfiniteContent(renderer, outputStream);
    }

    private static int getStubLength(final HttpServletRequest request) {
        final String userAgent = request.getHeader("User-Agent");
        if ( userAgent == null ) {
            return 0;
        }
        if ( userAgent.contains("Chrome") ) {
            return 1024;
        }
        if ( userAgent.contains("Firefox") ) {
            return 1024;
        }
        return 0;
    }

    private static void renderInfiniteContent(final IRenderer renderer, final ServletOutputStream outputStream)
            throws IOException, InterruptedException {
        for(;;) {
            for ( final String line : lines ) {
                renderer.renderLine(line, outputStream);
                Thread.sleep(5000);
            }
        }
    }

    private interface IRenderer {
        void renderStub(int length, ServletOutputStream outputStream)
                throws IOException;

        void renderLine(String line, ServletOutputStream outputStream)
                throws IOException;
    }

    private enum Renderers implements IRenderer {
        HTML {
            private static final String HTML_PREFIX = "<!-- ";
            private static final String HTML_SUFFIX = " -->";
            private final int HTML_PREFIX_SUFFIX_LENGTH = HTML_PREFIX.length() + HTML_SUFFIX.length();

            @Override
            public void renderStub(final int length, final ServletOutputStream outputStream)
                    throws IOException {
                outputStream.print(HTML_PREFIX);
                for ( int i = 0; i < length - HTML_PREFIX_SUFFIX_LENGTH; i++ ) {
                    outputStream.write('\u0020');
                }
                outputStream.print(HTML_SUFFIX);
                outputStream.flush();
            }

            @Override
            public void renderLine(final String line, final ServletOutputStream outputStream) throws IOException {
                outputStream.print(htmlEscape(line, "UTF-8"));
                outputStream.print("<br/>");
            }
        },

        PLAIN {
            private static final char ZERO_WIDTH_CHAR = '\u200B';
            private final byte[] bom = { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF };

            @Override
            public void renderStub(final int length, final ServletOutputStream outputStream)
                    throws IOException {
                outputStream.write(bom);
                for ( int i = 0; i < length; i++ ) {
                    outputStream.write(ZERO_WIDTH_CHAR);
                }
                outputStream.flush();
            }

            @Override
            public void renderLine(final String line, final ServletOutputStream outputStream)
                    throws IOException {
                outputStream.println(line);
                outputStream.flush();
            }
        }

    }

}
