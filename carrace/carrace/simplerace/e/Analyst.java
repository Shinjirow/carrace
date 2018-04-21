/**
 * Analyst
 * 統計情報を記録、出力するクラス
 * @author takaesumizuki
 */

package simplerace.e;
import simplerace.*;

import java.util.Vector;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class Analyst{
		SensorModel inputs;
		double maxSpeed = -1;
		int turn = 0;
		int score = -1;
		final double EPS = 1e-10;
		double prevFlagX = -1.0;
		double prevFlagY = -1.0;
		private static int s = 0;
		private static int round = -1;
		static Vector<Integer> scoreHist = new Vector<Integer>();

		//追記
        private File file;
        private PrintWriter pw;


		Analyst(){
			Analyst.round++;
			// Analyst.scoreHist = new Vector<Integer>();
			if(Analyst.scoreHist.size() < this.decideMaxScore()) for(int i = 0; i < this.decideMaxScore(); i++) Analyst.scoreHist.add(Integer.valueOf(0));
		}
		public boolean isFinalRound(){
			int finalRoundNumber = 200;
			return Analyst.round == finalRoundNumber;
		}

		public void setInputs(SensorModel inputs){
			this.inputs = inputs;
		}

		public void update(SensorModel inputs){
			this.setInputs(inputs);
			this.updateMaxSpeed();
			this.turn++;
			if(this.isFlagChenged()) this.score++;
			if(this.isLastTurn()) this.doLastTurnProcess();
		}

		public void printResult(){
			System.out.println("---");
			System.out.println("| round : " + Analyst.round);
			System.out.println("| speed : " + this.inputs.getSpeed());
			System.out.println("---");
		}

		public boolean isLastTurn(){
			return this.turn == 1000;
		}

		public void finalRoundProcess(){
			System.out.println("finalRoundEnd");
			this.printScoreHist();
		}


		private boolean isFlagChenged(){
			if(prevFlagX == this.inputs.getNextWaypointPosition().x && prevFlagY == this.inputs.getNextWaypointPosition().y ){
				return false;
			}else {
				prevFlagX = this.inputs.getNextWaypointPosition().x;
				prevFlagY = this.inputs.getNextWaypointPosition().y;
				return true;
			}
		}

		

		private void recordScore(){
			Integer element = Analyst.scoreHist.get(this.score) + Integer.valueOf(1);
			Analyst.scoreHist.set(this.score, element);

			/* 4/18 追記 ファイルにスコアを書き出して200回以上でのデータ取りをできるように */
			this.putResults(this.score);
		}

		private void printScoreHist(){
			int maxScore = this.decideMaxScore();
			for(int i = 0; i < 40; i++){
				String output = "Score" + i;
				if(i < 10) output += "  : ";
				else output += " : ";
				for(int j = 0; j < Analyst.scoreHist.get(i); j++){
					output += "|";
				}
				System.out.println(output);
			}
			System.out.println(Analyst.scoreHist.size());

		}

		private int decideMaxScore(){
			return 40;
		}

		private void updateMaxSpeed(){
			if(this.maxSpeed < Math.abs(this.inputs.getSpeed())){
				this.maxSpeed = Math.abs(this.inputs.getSpeed());
			}
		}

		private void doLastTurnProcess(){
			Analyst.s += this.score;
			this.recordScore();
			if(this.isFinalRound()) this.finalRoundProcess();
		}

		// 追記したやつ
        private void putResults(Integer e){
		    try{
                this.file = new File("results.txt");
                this.pw = new PrintWriter(new BufferedWriter(new FileWriter(file,true)));
                this.pw.println(e);
                this.pw.close();
            }catch(IOException exception){
		        exception.printStackTrace();
            }
        }

}
