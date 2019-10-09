import java.io.*;
import java.util.Scanner;

public class EquipmentEditor
{

    public static int targetSlots[] = new int[23000];

    public EquipmentEditor()
    {
    }

    public static void main(String args[])
    {
        loadEquipmentData();
        Scanner s = new Scanner(System.in);
        System.out.println((new StringBuilder("TEST: ")).append(targetSlots[11694]).toString());
        do
        {
            String line = s.nextLine();
            if(!line.equalsIgnoreCase("end"))
            {
                int id = Integer.parseInt(line.split(" ")[0]);
                int slot = Integer.parseInt(line.split(" ")[1]);
                System.out.println((new StringBuilder("Item: ")).append(id).append(" now equips to slot: ").append(slot).toString());
                targetSlots[id] = slot;
                saveEquipmentData();
            } else
            {
                saveEquipmentData();
                return;
            }
        } while(true);
    }

    public static void loadEquipmentData()
    {
        try
        {
            File f = new File("./equipment.dat");
            FileInputStream fis = new FileInputStream(f);
            int counter = 0;
            for(int slot = -1; (slot = fis.read()) != -1;)
            {
                targetSlots[counter++] = slot;
            }

            fis.close();
        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

    public static void saveEquipmentData()
    {
        try
        {
            File f = new File("./equipment.dat");
            FileOutputStream fos = new FileOutputStream(f);
            int counter = 0;
            int slot = -1;
            for(int i = 0; i < targetSlots.length; i++)
            {
                fos.write(targetSlots[i]);
            }

        }
        catch(IOException ioe)
        {
            ioe.printStackTrace();
        }
    }

}
