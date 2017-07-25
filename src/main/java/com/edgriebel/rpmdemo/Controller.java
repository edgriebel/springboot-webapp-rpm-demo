package com.edgriebel.rpmdemo;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping(value="/rest", method=RequestMethod.GET) // all APIs only allow GET unless exceptions in signature
@Api(value="spring-boot-rpm-demo", description="Demo app for RPM")
public class Controller {
    private final static Logger logger = Logger.getLogger(Controller.class.getName());
    
    @Value("${hello.values}")
    private String [] helloValues;

    @Autowired
    private StatusChecker statusChecker;
    
    @ApiOperation(value="DemoDO Info",
            notes="Return DemoDO object with enabled/disabled")
    @RequestMapping("/status")
    public DemoDO getStatus() {
        logger.info("getStatus() called");
        return statusChecker.getStatus();
    }

    @ApiOperation("Hello World!")
    @RequestMapping("/hello")
    public DemoDO getHello(
            @RequestParam(required=false) String name) 
    {
        logger.info("getHello() called, name=" + name);
        return statusChecker.getHello(name);
    }

    @ApiOperation(value="Do some stuff",
            notes="Return permutations of foo, bar, and baz", 
            httpMethod="POST")
    @RequestMapping(value = "/stuff/{fooName}/{barName}/{bazName}", method = RequestMethod.POST)
    public Collection<DemoDO> doStuff(
            @PathVariable String fooName,
            @PathVariable String barName,
            @PathVariable String bazName
            )
    {
        logger.info(String.format("doStuff() called with %s:%s:%s", fooName, barName, bazName));
        if (fooName == null || fooName.isEmpty() || barName == null || barName.isEmpty() || bazName == null || bazName.isEmpty())
            throw new IllegalArgumentException("foo, bar, and baz must be populated");
        
        Collection<DemoDO> rtn = statusChecker.doStuff(fooName, barName, bazName);
        
        return rtn;
    }

    @ApiIgnore // hide from Swagger UI 
    @RequestMapping("/test")
    public String doTest(@RequestParam String value) {
        return IntStream.rangeClosed(0, (int)(Math.random()*10)+1)
                .mapToObj(i -> i + ":" + value + " ")
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        logger.warning(e.getMessage());
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(IllegalAccessError.class)
    void handleIllegalAccessError(IllegalAccessError e, HttpServletResponse response) throws IOException 
    {
        logger.warning(e.getMessage());
        // XXX hardcoding here because all the methods in this class that change data only accept POST 
        response.addHeader(HttpHeaders.ACCEPT, "POST");
        response.sendError(HttpStatus.METHOD_NOT_ALLOWED.value(), e.getMessage());
    }
}
