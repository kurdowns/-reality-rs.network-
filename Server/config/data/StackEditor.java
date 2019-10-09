import java.io.*;
import java.util.Scanner;

public class StackEditor
{

    public static int targetSlots[] = new int[21000];

    public StackEditor()
    {
    }

    public static void main(String args[])
    {
        loadStackableData();
        Scanner scanner = new Scanner(System.in);
        System.out.println("The format is: itemid 0/1");
        System.out.println("Example: To make Armadyl GodSwords stackable, you would put 11694 1, to make it " +
"not stack, you would put 11694 0"
);
        do
        {
            String s = scanner.nextLine();
            if(!s.equalsIgnoreCase("end"))
            {
                int i = Integer.parseInt(s.split(" ")[0]);
                int j = Integer.parseInt(s.split(" ")[1]);
                System.out.println((new StringBuilder()).append("The item ").append(i).append(" is now ").append(j).append(" in ability to stack. 1 - Can be stacked, 0 - Can't be").toString());
                targetSlots[i] = j;
                saveStackableData();
            } else
            {
                saveStackableData();
                return;
            }
        } while(true);
    }

    public static void loadStackableData()
    {
        try
        {
            File file = new File("./stackable.dat");
            FileInputStream fileinputstream = new FileInputStream(file);
            int i = 0;
            for(int j = -1; (j = fileinputstream.read()) != -1;)
            {
                targetSlots[i++] = j;
            }

            fileinputstream.close();
        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

    public static void saveStackableData()
    {
        try
        {
            File file = new File("./stackable.dat");
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            boolean flag = false;
            byte byte0 = -1;
            for(int i = 0; i < targetSlots.length; i++)
            {
                fileoutputstream.write(targetSlots[i]);
            }

        }
        catch(IOException ioexception)
        {
            ioexception.printStackTrace();
        }
    }

}