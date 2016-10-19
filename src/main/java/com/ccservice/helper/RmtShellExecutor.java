package com.ccservice.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;


public class RmtShellExecutor {
    private Connection conn;
    private String ip;

    private int port;

    private String usr;

    private String psword;

    private String charset = Charset.defaultCharset().toString();

    private static final int TIME_OUT = 1000 * 5 * 60;

    public RmtShellExecutor(ShellParam param) {
        this.ip = param.getIp();
        this.usr = param.getUsername();
        this.psword = param.getPassword();
    }

    class ShellParam {
        String ip;

        String usr;

        String psword;

        String getIp() {
            return ip;
        };

        String getUsername() {
            return usr;
        }

        String getPassword() {
            return psword;
        }
    }

    public RmtShellExecutor(String ip, String usr, String ps, int port) {
        this.ip = ip;
        this.usr = usr;
        this.psword = ps;
        this.port = port;
    }

    private boolean login() throws IOException {
        conn = new Connection(ip, port);
        conn.connect();
        return conn.authenticateWithPassword(usr, psword);
    }

    public String exec(String cmds) throws Exception {
        InputStream stdOut = null;
        InputStream stdErr = null;
        String outStr = "";
        String outErr = "";
        int ret = -1;
        try {
            if (login()) {
                // Open a new {@link Session} on this connection
                Session session = conn.openSession();
                // Execute a command on the remote machine.
                session.execCommand(cmds);
                stdOut = new StreamGobbler(session.getStdout());
                outStr = processStream(stdOut, charset);
                stdErr = new StreamGobbler(session.getStderr());
                outErr = processStream(stdErr, charset);
                session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);
                ret = session.getExitStatus();
            }
            else {
                System.out.println("登录远程机器失败" + ip);
            }
        }
        finally {
            if (conn != null) {
                conn.close();
            }
        }
        return outStr;
    }

    private String processStream(InputStream in, String charset) throws Exception {
        byte[] buf = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while (in.read(buf) != -1) {
            sb.append(new String(buf, charset));
        }
        return sb.toString();
    }

    public static void main(String args[]) throws Exception {
        RmtShellExecutor exe = new RmtShellExecutor("121.40.251.102 ", "root", "5n0wbIrdsMe1webhthynbdehenya", 22);
        System.out.println(exe.exec("ps -aux|grep tomcat"));
        System.out.println(exe.exec("kill -9 18460"));
        System.out.println(exe.exec("/etc/init.d/tomcat7 restart"));
        System.out.println(exe.exec("tailf ../alidata/server/tomcat7/logs/catalina.out"));
        
        
        
    }
}
