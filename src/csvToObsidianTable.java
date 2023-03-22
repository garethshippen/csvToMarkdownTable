/*
Input:
csv table needs the format
left, right, centre,... alignment choice
data, data, data,...

Output:
Obsidian tables have the form
|data|data|data|....|
|:---|:--:|---:|....| alignment options
|data|data|data|....|
|data|data|data|....|
 */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class csvToObsidianTable
{
    private final File FILE;
    private String[] row;
    private ArrayList<String[]> rows = new ArrayList<>();
    private static StringBuilder table = new StringBuilder();
    public csvToObsidianTable(String _file)
    {
        FILE = new File(_file);
        //Get the number of columns in the csv
        int columns = getColumns(FILE);

        //Exit program if there is something weird with the csv
        if(columns > 0)
        {
            System.out.println("Invalid csv.");
            System.exit(1);
        }
        row = new String[columns];

        getData();
    }
    public static void main(String[] args)
    {
        csvToObsidianTable runIt = new csvToObsidianTable(getFile(args));
        runIt.genTable();
        runIt.displayTable();
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

            while(!path.equalsIgnoreCase("q") || !path.endsWith("csv"))
            {
                System.out.println("Please enter path to a csv file, q to quit.");
                path = input.nextLine();
            }
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

        //region Alignment
        String[] row = rows.get(0); //This row has the alignment instructions
        StringBuilder alignmentOption = new StringBuilder("|");
        for(String cell: row)
        {
            switch (cell.toLowerCase())
            {
                case ("l"),("left") ->
                {
                    alignmentOption.append(":--|");
                }
                case ("r"),("right") ->
                {
                    alignmentOption.append("--:|");
                }
                case ("c"),("center"), ("centre") ->
                {
                    alignmentOption.append(":-:|");
                }
                default ->
                {
                    alignmentOption.append("---|"); //If there is no alignment instructions
                }
            }
        }
        alignmentOption.append("\n");
        //endregion

        //region Headers
        row = rows.get(1);
        StringBuilder rowBuilder = new StringBuilder("|");
        for(String cell: row)
        {
            rowBuilder.append(cell).append("|");
        }
        rowBuilder.append("\n");
        //endregion

        table.append(rowBuilder).append(alignmentOption);

        int numberOfRows = rows.size();

        for(int i = 2; i < numberOfRows; i++)
        {
            row = rows.get(i);
            rowBuilder.append("|");
            for(String cell: row)
            {
                rowBuilder.append(cell).append("|");
            }
            rowBuilder.append("\n");
        }
    }
}
//TODO Support commas in the cells