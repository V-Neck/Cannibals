/* Vaughn Johnson
   28 July 2016

   This class recursively solves the "Cannibals and missionaries problem".
   From Wikipedia:

   In the missionaries and cannibals problem, three missionaries and three cannibals must cross a river using a boat which can carry at most two people, under the constraint that, for both banks, if there are missionaries present on the bank, they cannot be outnumbered by cannibals (if they were, the cannibals would eat the missionaries). The boat cannot cross the river by itself with no people on board. And, in some variations, one of the cannibals has only one arm and cannot row.
   */

import java.util.*;
import java.io.*;

public class Cannibal{

/* k is the number of cannibals or missionaries. The current configuration assumes equal numbers of both, but futures could includes different numbers of each
   */
   static private int k;

   //Capacity of boat
   static private int cap;

/* This will contain the list of moves neccasary to complete the problem
   The stack is filled with two demmensional Int arrays, which denote how many missionaries and how many cannibals are to be moved (keep in mind that the boat switches sides each move). Missionaries are written first, and cannibals second. For example, the trival case of no one moving is [0,0]. The case of one missionary and one cannibal moving over, and two cannibals coming back would be denoted as [1,1],[0,2]
   */
   static private Stack<int[]> moveStack;
/*To contain the final set of moves neccasary to complete the problem.
   */
   static private List<int[]> solution;

/* This keeps track of all of the used combinations of missionaries on the left side, cannibals on the left side, and which side the canoe is on. Those used states are stored as a string in the format "[missionaries][canniblas][side]".
   */
   static private Set<String> stateSet;

   public static void main(String[] args){
      k = Integer.parseInt(args[0]);
      cap = Integer.parseInt(args[1]);
      moveStack = new Stack<int[]>();
      solution = new ArrayList<int[]>();
      stateSet = new HashSet<String>();

      solve(k, k);

      for(int[] move : solution){
         System.out.println(Arrays.toString(move));
      }
   }

/* See public solve(int missLeft, int cannLeft, int side). Begins on the left side, denoted by "1".
   */
   public static void solve(int missLeft, int cannLeft){
      solve(missLeft, cannLeft, 1);
   }

/*This function takes the number of missionaries on the left (missLeft), the number of cannibals on the left (cannLeft), which side the canoe is on (side). It generates all of the possible moves that can be made. It then makes one move, and calls the solve function on thew new state generated form making that move. It then checks if all the cannibals and missionaires      are on the right, and if not, tries the next possible move
   */
   private static void solve(int missLeft, int cannLeft, int side){
      stateSet.add("" + missLeft + cannLeft + side);
      List<int[]> possibleMoves = new ArrayList<int[]>();

      if((cannLeft + missLeft) != 0){
         //m = how many missionaires are in teh boat
         for(int m = 0; m <= cap; m++){
            //c = how many canniblas are in teh boat
            for(int c = 0; c <= cap-m; c++){
               if( isPossible(missLeft, cannLeft, m, c, side) ){
                  possibleMoves.add(new int[]{m, c});
               }
            }
         }

         /*for(int[] move : moveStack){
            System.out.println(Arrays.toString(move) + " move stack");
         }

         for(int[] move : possibleMoves){
            System.out.println(Arrays.toString(move) + " possible moves");
         }*/

         Collections.shuffle(possibleMoves);

         for(int[] move : possibleMoves){
            moveStack.push(move);

            if(side == 1 && !stateSet.contains("" + (missLeft-move[0]) + (cannLeft-move[1]) + (side*-1) )){
               solve(missLeft-move[0], cannLeft-move[1], side*-1);
            } else if(!stateSet.contains("" + (missLeft+move[0]) + (cannLeft+move[1]) + (side*-1) )){
               solve(missLeft+move[0], cannLeft+move[1], side*-1);
            }
            if(!moveStack.isEmpty()){
               moveStack.pop();
            }
         }
      } else {
         for(int[] move : moveStack){
            solution.add(move);
         }
      }
   }

/*Takes the number of missionaires and cannibals on teh left (missLeft & cannLeft), and the proposed number of cannibals and missionaires to be moves on teh boat (m, c), as well as which direct the missionaries/cannibals will be moved (side).
   */
   public static boolean isPossible(int missLeft, int cannLeft, int m, int c, int side){
      //missRight = missionaries on the right
      int missRight = k - missLeft;
      //cannRight = cannibals on the right
      int cannRight = k - cannLeft;

      //Check null
      if ((m + c) == 0){
         return false;
      }

      //Moves people across river
      if(side == 1){
         missLeft -= m;
         cannLeft -= c;
         missRight += m;
         cannRight += c;

      } else{
         missLeft += m;
         cannLeft += c;
         missRight -= m;
         cannRight -= c;
      }

      //Checks against negatives
      if(missLeft < 0 || cannLeft < 0 || missRight < 0 || cannRight < 0){
         return false;
      }

      return ((missLeft>=cannLeft) || (missLeft==0)) && ((missRight>=cannRight) || (missRight==0));
   }
}
