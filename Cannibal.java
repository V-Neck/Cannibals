import java.util.*;
import java.io.*;

/*This class is meant to recursively solve the "Cannibals and missionaries problem".
   From Wikipedia:

   In the missionaries and cannibals problem, three missionaries and three cannibals must cross a river using a boat which can carry at most two people, under the constraint that, for both banks, if there are missionaries present on the bank, they cannot be outnumbered by cannibals (if they were, the cannibals would eat the missionaries). The boat cannot cross the river by itself with no people on board. And, in some variations, one of the cannibals has only one arm and cannot row.
*/
public class Cannibal{

   /*K is the number of cannibals or missionaries.
   The current configuration assumes equal numbers
   of both, but futures could includes different numbers of each
   */
   static private int k;

   //Capacity of boat
   static private int cap;

   /*This will contain the list of moves neccasary to complete the problem
   The stack is filled with two demmensional Int arrays, which denote how many missionaries and how many cannibals are to be moved (keep in mind that the boat switches sides each move). Missionaries are written first, and cannibals second. For example, the trival case of no one moving is [0,0]. The case of one missionary and one cannibal moving over, and two cannibals coming back would be denoted as [1,1],[0,2]
   */
   static private Stack<int[]> moveStack;

   static private Set<String> stateSet;

   public static void main(String[] args){
      k = 2;
      cap = 2;
      moveStack = new Stack<int[]>();
      stateSet = new HashSet<String>();

      //This solves a fencepost problem, but is inelegant
      moveStack.push( (new int[]{0, 0}) );

      solve(k, k);

   }

   //Public part of public/private pair
   public static void solve(int mL, int cL){
      solve(mL, cL, 1, 0);
   }

   /*git*/

   /*This function takes the number of missionaries on the left (mL), the number of cannibals on the left (cL), which side the canoe is on (side), and a number iter (used for debugging, shows what level the "tree" of possibilities is on). It generates all of the possible moves that can be made. It then makes one move, and calls the solve function on thew new state generated form making that move. It then checks if all the cannibals and missionaires are on the right, and if not, tries the next possible move*/
   private static void solve(int mL, int cL, int side, int iter){
      stateSet.add("" + mL + cL + side);
      List<int[]> possibleMoves = new ArrayList<int[]>();

      if((cL + mL) != 0){
         //m = how many missionaires are in teh boat
         for(int m = 0; m <= cap; m++){
            //c = how many canniblas are in teh boat
            for(int c = 0; c <= cap-m; c++){
               if( isPossible(mL, cL, m, c, side) ){
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

            if(side == 1 && !stateSet.contains("" + (mL-move[0]) + (cL-move[1]) + (side*-1) )){
               solve(mL-move[0], cL-move[1], side*-1, iter+ 1);
            } else if(!stateSet.contains("" + (mL+move[0]) + (cL+move[1]) + (side*-1) )){
               solve(mL+move[0], cL+move[1], side*-1, iter+ 1);
            }

            moveStack.pop();
         }
      }
      if((cL + mL) == 0){
         for(int[] move : moveStack){
            System.out.println(Arrays.toString(move));
         }
         System.out.println();
      }
   }

   /*Takes the number of missionaires and cannibals on teh left (mL & cL), and the proposed number of cannibals and missionaires to be moves on teh boat (m, c), as well as which direct the missionaries/cannibals will be moved (side).
   */
   public static boolean isPossible(int mL, int cL, int m, int c, int side){
      //mR = missionaries on the right
      int mR = k - mL;
      //cR = cannibals on the right
      int cR = k - cL;

      //Check null
      if ((m + c) == 0){
         return false;
      }

      //Moves people across river
      if(side == 1){
         mL -= m;
         cL -= c;
         mR += m;
         cR += c;

      } else{
         mL += m;
         cL += c;
         mR -= m;
         cR -= c;
      }

      //Checks against negatives
      if(mL < 0 || cL < 0 || mR < 0 || cR < 0){
         return false;
      }

      return ((mL>=cL) || (mL==0)) && ((mR>=cR) || (mR==0));
   }
}
