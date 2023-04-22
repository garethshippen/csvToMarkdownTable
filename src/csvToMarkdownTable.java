/*
Takes a csv file and converts it to a markdown table.
CSV FILE MUST BE SAVED IN UTF8

Input:
csv table needs the format
left, right, centre,... alignment choice
column heading, column heading, column heading,...
data, data, data,...

Output:
|data|data|data|....|
|:---|:--:|---:|....| alignment options
|data|data|data|....|
|data|data|data|....|
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

public class csvToMarkdownTable
{
    private final File FILE;
    private String[] row;
    private ArrayList<String[]> rows = new ArrayList<>();
    private static StringBuilder table = new StringBuilder("|");


    //Constructor
    public csvToMarkdownTable(String _file)
    {
        FILE = new File(_file);
        //Get the number of columns in the csv
        int columns = getColumns(FILE);

        //Exit program if there is something weird with the csv
        if(columns <= 0)
        {
            System.out.println("Invalid csv.");
            System.exit(1);
        }
        row = new String[columns];

        getData();
    }

    //Outputs the formatted table to the console.
    private void displayTable()
    {
        System.out.println(table);
        //TODO add copy to clipboard
    }

    //Gets the file to be converted.
    private static String getFile(String[] args)
    {
        String path = "awaiting input";
        if(args.length == 1 && args[0].endsWith("csv"))
        {
            path = args[0];
        }
        else
        {
            Scanner input = new Scanner(System.in);

            while(!path.equalsIgnoreCase("q") && !path.endsWith("csv"))
            {
                System.out.println("Please enter path to a csv file, or enter 'q' to quit.");
                path = input.nextLine();
            }
        }
        //If quit, terminate program.
        if(path.equals("q"))
        {
            System.out.println("Goodbye");
            System.exit(0);
        }
        return path;
    }

    //Reads the csv file and puts the data into the 'rows' ArrayList.
    private void getData()
    {
        //Read input and store
        try
        {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(FILE), StandardCharsets.UTF_8));
            //BufferedReader bf = new BufferedReader(new FileReader(FILE));
            final String DELIMITER = ",";
            String line;
            while((line = bf.readLine()) != null)
            {
                row = line.split(DELIMITER);
                rows.add(row);
            }
            bf.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        } catch (IOException e)
        {
            System.out.println("Error: IOException.");
            e.printStackTrace();
        }
    }

    //Returns the number of columns in the table.
    //TODO merge with genTable()?
    private int getColumns(File _file)
    {
        int noOfColumns = 0;
        try
        {
            BufferedReader bf = new BufferedReader(new InputStreamReader(new FileInputStream(_file), StandardCharsets.UTF_8));
            //BufferedReader bf = new BufferedReader(new FileReader(_file));
            String input = bf.readLine();
            String[] columns = input.split(",");
            noOfColumns = columns.length;
            bf.close();
        }
        catch (FileNotFoundException e)
        {
            System.out.println("Error: File not found.");
            e.printStackTrace();
        } catch (IOException e)
        {
            System.out.println("Error: IOException.");
            e.printStackTrace();
        }

        return noOfColumns;
    }

    //Generates the table in mark down.
    //TODO Handle csv files with no alignment row.
    private void genTable()
    {
        final int ALIGNMENT_ROW = 0;
        final int HEADER_ROW = ALIGNMENT_ROW + 1;
        final int FIRST_DATA_ROW = ALIGNMENT_ROW + 2;
        String[] row;
        //region Headers
        row = rows.get(HEADER_ROW);
        for(String cell: row)
        {
            table.append(cell.trim()).append("|");
        }
        table.append("\n");
        //endregion

        //region Alignment
        row = rows.get(ALIGNMENT_ROW);
        table.append("|");
        for(int i = 0; i < row.length; i++)
        {
            String alignment = row[i].toLowerCase().trim();
            switch(alignment)
            {
                case "l":
                case "left":
                {
                    table.append(":--|");
                    break;
                }
                case "r":
                case "right":
                {
                    table.append("--:|");
                    break;
                }
                case "c":
                case "center":
                case "centre":
                {
                    table.append(":-:|");
                    break;
                }
                default:
                {
                    table.append("---|"); //If there is no alignment instructions
                }
            }
        }
        table.append("\n");
        //endregion

        //region Remaining Rows
        int numberOfRows = rows.size();

        for(int i = FIRST_DATA_ROW; i < numberOfRows; i++)
        {
            row = rows.get(i);
            table.append("|");
            for(String cell: row)
            {
                table.append(cell.trim()).append("|");
            }
            table.append("\n");
        }
        //endregion
    }

    public static void main(String[] args)
    {
        //String[] test = {"path to debugging csv"};
        //csvToMarkdownTable runIt = new csvToMarkdownTable(getFile(test));
        csvToMarkdownTable runIt = new csvToMarkdownTable(getFile(args));
        runIt.genTable();
        runIt.displayTable();
    }
}
//TODO Support commas in the cells