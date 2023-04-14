/*
Input:
csv table needs the format
left, right, centre,... alignment choice
data, data, data,...

Output:
|data|data|data|....|
|:---|:--:|---:|....| alignment options
|data|data|data|....|
|data|data|data|....|
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class csvToMarkdownTable
{
    private final File FILE;
    private String[] row;
    private ArrayList<String[]> rows = new ArrayList<>();
    private static StringBuilder table = new StringBuilder("|");
    public csvToMarkdownTable(String _file)
    {
        FILE = new File(_file);
        //Get the number of columns in the csv
        int columns = getColumns(FILE);

        //Exit program if there is something weird with the csv
        //System.out.println("Columns " + columns);
        if(columns <= 0)
        {
            System.out.println("Invalid csv.");
            System.exit(1);
        }
        row = new String[columns];

        getData();
    }

    private void displayTable()
    {
        System.out.println(table);
        //TODO add copy to clipboard
    }

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
                System.out.println("Please enter path to a csv file, q to quit.");
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

    private void getData()
    {
        //Read input and store
        try
        {
            BufferedReader bf = new BufferedReader(new FileReader(FILE));
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

    private int getColumns(File _file)
    {
        int noOfColumns = 0;
        try
        {
            BufferedReader bf = new BufferedReader(new FileReader(_file));
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

    private void genTable()
    {
        String[] row;
        //region Headers
        row = rows.get(2);
        for(String cell: row)
        {
            table.append(cell.trim()).append("|");
        }
        table.append("\n");
        //endregion

        //region Alignment
        row = rows.get(1); //This row has the alignment instructions
        table.append("|");
        //TODO fix this
        //for(String cell: row)
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
//            if(alignment.equals("l") || alignment.equals("left"))
//            {
//                table.append(":--|");
//            }
//            else if(alignment.equals("r") || alignment.equals("right"))
//            {
//                table.append("--:|");
//            }
//            else if(alignment.equals("c") || alignment.equals("centre") || alignment.equals("center"))
//            {
//                table.append(":-:|");
//            }
//            else
//            {
//                table.append("---|"); //If there is no alignment instructions
//            }
        }
        table.append("\n");
        //endregion

        //region Remaining Rows
        int numberOfRows = rows.size();

        for(int i = 3; i < numberOfRows; i++)
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
        String[] test = {"C:\\Users\\Blinks\\Desktop\\test.csv"};
        csvToMarkdownTable runIt = new csvToMarkdownTable(getFile(test));
        //csvToMarkdownTable runIt = new csvToMarkdownTable(getFile(args));
        runIt.genTable();
        runIt.displayTable();
    }
}
//TODO Support commas in the cells