import java.util.*;

public class subset_construct {
    public static void main(String[] args) {
        String[] state = {"A","B","C","D","E","F","G","H"};
        String[] value = {"0","1"};
        String initialState = "A";
        String[] finalState = {"A","B","C","D","E","H"};
        String[][] transitionA = {{"AC","B"},{"DE","E"},{"AE","E"},{"FG","G"},{"GH","G"},{"H","H"},{"FH","FH"},{"H","H"}};
//        String[] state = {"A","B","C"};
//        String[] value = {"0","1","2","epsilon"};
//        String initialState = "A";
//        String[] finalState = {"C"};
//        String[][] transitionA = {{"A","-","-","B"},{"-","B","-","C"},{"-","-","C","-"}};

        subsetConstruction(transitionA, initialState, finalState, value, state);
//        System.out.println(union("A1","AbcV"));

    }



    static void subsetConstruction(String[][] T, String Q, String[] F, String[] V, String[] S)
    {
        int colLength = V.length;
        ArrayList<String> States = new ArrayList<String>();
        ArrayList<String> temp = new ArrayList<String>();
        String[][] Tran = new String[1][colLength];
        States.add(Q);
        int i = States.size()-1;
        int count = 0;
        for(int j = 0; j < colLength; j++)
        {   i = States.size()-1;
            count = 0;
            while(i < States.size())
            {
                for(int k = 0; k < States.get(count).length(); k++)
                {
                    for(int l = 0; l < S.length; l++)
                    {
                        //System.out.println(States.get(i));
                        if(States.get(count).charAt(k) == S[l].charAt(0))
                        {
                            System.out.print("States"+States.get(count).charAt(k)+" IState"+S[l]+" "+l+" ");
                            temp.add(T[l][j]);
                            System.out.println("temp"+T[l][j]);
                            break;

                        }
                    }
                }
                String tempString = temp.get(0);
                System.out.println("tempS "+tempString);
                if(temp.size() >= 1)
                {
                    for(int m = 1; m < temp.size(); m++)
                    {
                        tempString = union(tempString,temp.get(m));
                    }
                    System.out.println("finish "+tempString);
                }
                if(tempString.equals(""))
                {
                    Tran[count][j] = "\u2205";
                    i++; count++;
                }
                else
                {
                    System.out.println(i);
//                    Tran = extend2dArray(Tran,colLength);
                    Tran[count][j] = tempString;
                    boolean haha = false;
                    for(int a = 0; a < States.size(); a++)
                    {
                        if(!Tran[count][j].equals(States.get(a)))
                        {
                            haha = true;
                        }
                        else
                        {
                            haha = false; break;
                        }
                    }
                    if(haha)
                    {
                        States.add(Tran[count][j]);
                        Tran = extend2dArray(Tran,colLength);
                        haha = false;
//                        i = States.size()-1;
                    }
                    i++;count++;
                }
                temp.clear();
            }

        }


        for(int test = 0 ; test < Tran.length; test++)
        {
            for(int test2 = 0 ; test2 < Tran[test].length; test2++)
            {
                System.out.print(Tran[test][test2]+"\t");
            }
            System.out.println();
        }
        for(int aaa=0 ; aaa < States.size(); aaa++)
        {
            System.out.println(States.get(aaa));
        }
    }


    private static String[] extendArraySize(String [] array){
        String [] temp = array.clone();
        array = new String[array.length + 1];
        System.arraycopy(temp, 0, array, 0, temp.length);
        return array;
    }

    private static String[][] extend2dArray(String[][] array, int colLength){
        String[][] temp = array.clone();
        array = new String[array.length+1][colLength];
        for(int i = 0; i < temp.length; i++)
        {
            for(int j = 0; j < temp[i].length; j++)
            {
                array[i][j] = temp[i][j];
            }
        }
        //array = temp.clone();
        System.out.println("length "+array.length);
        return array;
    }

    public static String union (String s1, String s2)
    {
        // if either string is null, union is the other string
        if (s1 == null)
            return s2;

        if (s2 == null)
            return s1;

        // use linked set to keep ordering
        Set<String> unique = new LinkedHashSet<>();

        // put all words from string 1 into the set
        for (String word : s1.split(""))
        {
            word = word.trim(); // remove surrounding space on word
            unique.add(word);
        }

        // put all words from string 2 into the set
        for (String word : s2.split(""))
        {
            word = word.trim(); // remove surrounding space on word
            unique.add(word);
        }

        // get back the format of comma delimiter for the union
        String ret = unique.toString().replaceAll("[\\[\\] ]", "");
        ret = ret.replaceAll("[,\\/]","");
        return ret;
    }
}


