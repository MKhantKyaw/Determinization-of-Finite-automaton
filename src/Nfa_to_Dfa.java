import java.lang.reflect.Array;
import java.util.*;

public class Nfa_to_Dfa {
    public static void main(String[] args) {
        long start = System.nanoTime();

//        String[] state = {"A","B","C"};
//        String[] value = {"0","1","2","epsilon"};
//        String initialState = "A";
//        String[] finalState = {"C"};
//        String[][] transitionA = {{"A","-","-","B"},{"-","B","-","C"},{"-","-","C","-"}};

//        example 1
//        String[] state = {"A","B","C","D"};
//        String[] value = {"0","1","2","3","epsilon"};
//        String initialState = "A";
//        String[] finalState = {"D"};
//        String[][] transitionA = {{"A","-","-","-","B"},{"-","B","-","-","C"},{"-","-","C","-","D"},{"-","-","-","D","-"}};

        //example3
//        String[] state = {"A","B","C"};
//        String[] value = {"0","1","epsilon"};
//        String initialState = "A";
//        String[] finalState = {"C"};
//        String[][] transitionA = {{"BC","A","B"},{"-","B","C"},{"C","C","-"}};

//        example4
        String[] state = {"A","B","C","D","E","F","G","H"};
        String[] value = {"0","1"};
        String initialState = "A";
        String[] finalState = {"A","B","C","D","E","H"};
        String[][] transitionA = {{"AC","B"},{"DE","E"},{"AE","E"},{"FG","G"},{"GH","G"},{"H","H"},{"FH","FH"},{"H","H"}};

//        example5
//        String[] state = {"A","B","C"};
//        String[] value = {"0","1","epsilon"};
//        String initialState = "A";
//        String[] finalState = {"C"};
//        String[][] transitionA = {{"-","C","B"},{"-","-","C"},{"C","-","-"}};

//        example6
//        String[] state = {"A","B","C"};
//        String[] value = {"0","epsilon"};
//        String initialState = "A";
//        String[] finalState = {"C"};
//        String[][] transitionA = {{"-","B"},{"C","-"},{"-","-"}};


        System.out.println("\nThe NFA before epsilon removal is as follows:\n");
        outputNFA(state,value,finalState,transitionA);
        if(value[value.length - 1].equals("epsilon"))
        {
            String[][] Tran = epsilonRemove(state,value,initialState,finalState,transitionA);
            value = Arrays.copyOf(value, value.length-1);
            initialState = initialMake(initialState,Tran,state);
            subsetConstructionNew(Tran,finalState,initialState,state,value);
        }
        else
        {
            subsetConstructionNew(transitionA,finalState,initialState,state,value);
        }
//        String[][] Tran = epsilonRemove(state,value,initialState,finalState,transitionA);
//        subsetConstructionNew(Tran,finalState,initialState,state,value);
        long end = System.nanoTime();
        System.out.println("\nThe run time is "+(end-start)+" nanoseconds");
    }



    static String[][] epsilonRemove(String[] S, String[] V, String Q, String[] F,String[][] T){

        String[][] Tran = T.clone();
        int colLength = V.length;
        int rowLength = S.length;
        for(int i = 0; i < rowLength ; i++)
        {
            for(int j = 0; j < colLength-1 ; j++)
            {
                //state to state case
                if(S[i].equals(T[i][j])) // find whether the state go itself or not
                {
                    for(int l = i; l < rowLength; l++)
                    {
                        if (!T[l][colLength - 1].equals("-")) //find whether epsilon is exist or not
                        {
//                            Tran[i][j] += T[l][colLength - 1];
                            for (int k = 0; k < S.length; k++) // find the state where the epsilon of the current state goes
                            {
                                if (T[l][colLength - 1].equals(S[k]))
                                {
                                    Tran[i][j] += T[l][colLength - 1];
                                    break;
                                }
                            }
                        }
                    }
                }

                else if("-".equals(T[i][j]))
                {
                    Tran[i][j] = "";
                    if(!T[i][colLength-1].equals("-"))
                    {
                        int temp = 0;
                        int tempJ = j;
                        boolean flag = false;
                            for(int k = 0; k < rowLength; k++)
                            {
                                if(S[k].equals(T[i][colLength-1]))
                                {
                                    temp = k;
                                }
                            }
                        for(int ii = i; ii < rowLength; ii++)
                        {
                            for(int l = temp; l < rowLength; l++)
                            {
                                if(T[l][tempJ].equals(T[ii][colLength-1]) && !T[ii][colLength - 1].equals("-"))
                                {
                                    Tran[i][j] += T[ii][colLength-1]; //initial taken and forwarded
                                    tempJ++;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }


        Tran = optimizeGraph(S,V,F,Tran);
        System.out.println("\nThe NFA after epsilon removal is as follows\n");
        outputNFA(S,V,F,Tran);
        return Tran;
//        subsetConstruction(Tran, F, Q, S, V);
    }


//    ------------------------------------------------------------------------------------




        static void subsetConstructionNew(String[][] T, String[] F, String Q, String[] S, String[] V)
        {
            int colLength = V.length;
            ArrayList<String> States = new ArrayList<String>();
            ArrayList<String> temp = new ArrayList<String>();
            String[][] Tran = new String[0][colLength];
            States.add(Q);
            boolean emptyExists = false;
            int i = 0;
            while(States.size() > Tran.length)
            {
                for(int j = 0; j < colLength; j++)
                {
                    for(int k = 0; k < States.get(i).length(); k++)
                    {
                        for(int l = 0; l < S.length; l++)
                        {
                            if(States.get(i).charAt(k) == S[l].charAt(0))
                            {
                                temp.add(T[l][j]);break;
                            }
                        }
                    }
                    String tempString = temp.get(0);

                    if(temp.size() > 1)
                    {
                        for(int m = 1; m < temp.size(); m++)
                        {
                            tempString = union(tempString, temp.get(m));
                        }
                    }
                    if(tempString.equals(""))
                    {
                        if(j == 0)
                        {
                            Tran = extend2dArray(Tran,colLength);
                        }
                        Tran[i][j] = "\u2205";
                        emptyExists = true;
                    }
                    else
                    {
                        if(j == 0)
                        {
                            Tran = extend2dArray(Tran,colLength);
                        }
                        Tran[i][j] = tempString;
                        boolean haha = false;
                        for(int a = 0; a < States.size(); a++)
                        {
                            if(!Tran[i][j].equals(States.get(a)))
                            {
                                haha = true;
                            }
                            else
                            {
                                haha = false;
                                break;
                            }
                        }
                        if(haha)
                        {
                            States.add(Tran[i][j]);
                            haha = false;
                        }

                    }
                    temp.clear();
                }
                i++;
            }
            if(emptyExists)
            {
                States.add("\u2205");
                Tran = extend2dArray(Tran,colLength);
                for(int aa = 0; aa < colLength; aa++)
                {
                    Tran[Tran.length-1][aa] = "\u2205";
                }
            }
            outputDFA(Tran,F, States, V);

        }







    private static String[][] extend2dArray(String[][] a, int colLength){
        String[][] temp = a.clone();
        a = new String[a.length+1][colLength];
        for(int i = 0; i < temp.length; i++)
        {
            for(int j = 0; j < temp[i].length; j++)
            {
                a[i][j] = temp[i][j];
            }
        }
        //array = temp.clone();
//        System.out.println("length "+array.length);
        return a;
    }

    private static String stringSort(String s)
    {
        char temp[] = s.toCharArray();
        Arrays.sort(temp);
        s = new String(temp);
        return s;
    }


    public static String union (String s1, String s2)
    {
        if (s1 == null)
            return s2;

        if (s2 == null)
            return s1;

        Set<String> unique = new LinkedHashSet<>();

        for (String word : s1.split(""))
        {
            word = word.trim();
            unique.add(word);
        }

        for (String word : s2.split(""))
        {
            word = word.trim();
            unique.add(word);
        }

        String ret = unique.toString().replaceAll("[\\[\\] ]", "");
        ret = ret.replaceAll("[,\\/]","");
        ret = stringSort(ret);
        return ret;
    }





        static void outputDFA(String[][] T, String[] F, ArrayList<String> S, String[] V){
            System.out.println("\nThe DFA after subset construction is as follows:\n");
            System.out.print("States\t");
            for(int h = 0; h < V.length; h++ )
            {
                System.out.print(V[h]+"\t\t");
            }
            System.out.println();
            for(int i = 0; i < T.length; i++)
            {
                if(S.get(i).length() > 3)
                {
                    System.out.print(S.get(i)+"\t");
                }
                else
                    System.out.print(S.get(i)+"\t\t");
                for(int j = 0; j < T[i].length; j++)
                {
                    for(int k = 0; k < F.length; k++)
                    {
                        if(T[i][j].contains(F[k]))
                        {
                            T[i][j] += "*";break;
                        }
                    }
                    if(T[i][j].length() > 3)
                    {
                        System.out.print(T[i][j]+"\t");
                    }
                    else
                        System.out.print(T[i][j]+"\t\t");

                }
                System.out.println();
            }
        }




        static String[][] optimizeGraph(String[] S, String[] V, String[] F, String[][] T){
            String[][] newT = new String[S.length][V.length-1];
            for(int i = 0; i < S.length; i++)
            {
                for(int j = 0; j < V.length-1; j++)
                {
                    newT[i][j] = T[i][j];
                }
            }
            return newT;
        }


//        -------------------------------------------------------------------------------------


    static void outputNFA(String[] S, String[] V, String[] F,String[][] T) {
        System.out.print("States\t");
        for(int a = 0; a < V.length; a++){
            System.out.print(V[a]+"\t\t");
        }
        System.out.println();

            for(int j = 0; j < T.length; j++)
            {
                System.out.print(S[j]);
                    for(int k =0; k < T[j].length; k++)
                    {
                        System.out.print("\t\t"+T[j][k]);
                    }
                System.out.println();
            }
    }


    private static String initialMake(String Q, String[][] T,String[] S)
    {
        boolean flag = true;
        for(int i = 0; i < T.length; i++)
        {
            if(!flag)
            {
                Q = S[i];break;
            }
            for(int j = 0; j < T[i].length; j++)
            {
                if(T[i][j].equals(""))
                {
                    flag = false;
                }
                else
                {
                    flag = true;
                    break;
                }
            }
        }
        return Q;
    }



//    static void subsetConstruction(String[][] T, String[] F, String Q, String[] S, String[] V){
//            //add states from initial state
//            boolean flag = false;
//            ArrayList<String> States = new ArrayList<String>();
//            States.add(Q);
//            //New States Find
//            for(int i = 0; i < T.length; i++)
//            {
//                for(int j = 0; j < T[i].length; j++)
//                {
//                    flag = false;
//                    for(int k = 0; k < States.size(); k++)
//                    {
//                        if(!States.get(k).equals(T[i][j])) flag = true;
//                        else
//                        {
//                            flag = false;
//                            break;
//                        }
//                    }
//                    if(flag && !T[i][j].equals(""))
//                    {
//                        States.add(T[i][j]);
//                    }
//                }
//            }
//
//            String[][] Tran = new String[States.size()][T[0].length];  //initialize for final graph
//            int rowLength = States.size();
//            int colLength = T[0].length;
//            ArrayList<String> temp = new ArrayList<String>();
//            for(int i = 0; i < States.size(); i++)
//            {
//                for(int j = 0; j < colLength; j++)
//                {
//                    for(int k = 0; k < States.get(i).length(); k++) // looping for concated states
//                    {
//                        for (int l = 0; l < S.length; l++)      // find concated states in individual states
//                        {
//                            if(States.get(i).charAt(k) == S[l].charAt(0))
//                            {
//                                temp.add(T[l][j]);break;
//                            }
//                        }
//                    }
//
//                    String tempString = temp.get(0);
//
//                    if(temp.size() !=1)
//                    {
//                        for(int m = 1; m < temp.size(); m++)
//                        {
//                            if(!tempString.contains(temp.get(m)))
//                            {
//                                tempString = temp.get(m);
//                            }
////                            if(tempString.equals("") || temp.get(m).equals(""))
////                            {
////                                if(temp.get(m).contains(tempString))
////                                {
////                                    tempString = temp.get(m);
////                                }
////                            }
////                            else
////                            {
////                                if(tempString.contains(temp.get(m)))
////                                {
////                                    tempString = temp.get(m);
////                                }
////                            }
//                        }
//                    }
//
//                    if(tempString.equals("")) Tran[i][j] = "\u2205";
//                    else Tran[i][j] = tempString;
//                    temp.clear(); // temp clear for next state value;
//                }
//            }
//
//            outputDFA(Tran,F, States, V);
//        }

}
