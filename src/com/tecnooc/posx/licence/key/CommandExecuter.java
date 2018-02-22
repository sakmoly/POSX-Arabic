package com.tecnooc.posx.licence.key;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by farhan on 17/8/14.
 */
public class CommandExecuter {

    /**
     * Execute the command in a process and search the output for a pattern.
     *
     * @param command
     * @param patternToFind
     * @return String - line in output which matches the pattern.
     * @throws IOException
     * @throws InterruptedException
     */
    public String executeAndFindPattern(String command, String patternToFind) throws IOException, InterruptedException {

        BufferedReader reader = executeAndGetReader(command);

        Pattern p = Pattern.compile(patternToFind);
        String line;
        Matcher m;

        while ((line = reader.readLine()) != null) {
            m = p.matcher(line);
            if (!m.find())
                continue;
            line = m.group();
            break;
        }
        return line;
    }

    /**
     * Execute the command in a process and search the output for a keyword.
     *
     * @param command
     * @param key
     * @return String - line in output which contains the key.
     * @throws IOException
     * @throws InterruptedException
     */
    public String executeAndFindLineWithKey(String command, String key) throws IOException, InterruptedException {

        BufferedReader reader = executeAndGetReader(command);

        String line;

        while ((line = reader.readLine()) != null) {
            if (line.contains(key))
                return line;
        }

        return null;
    }

    /**
     * Execute the command in a process and return the output as a String
     *
     * @param command
     * @return String - Output of the command
     * @throws IOException
     * @throws InterruptedException
     */
    public String execute(String command) throws IOException, InterruptedException {

        BufferedReader reader = executeAndGetReader(command);

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }

        return sb.toString();
    }

    /**
     * Execute the command in a process and return the BufferedReader for the process
     *
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    private BufferedReader executeAndGetReader(String command) throws IOException, InterruptedException {
        Process pa = Runtime.getRuntime().exec(command);
        pa.waitFor();

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                pa.getInputStream()));
        return reader;
    }

}
