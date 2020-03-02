import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.util.*;

class Player {
	public final static int human = 1;
	public final static int computer = 2;
	private int turn;
	private int type;

	Player(int j, int t) {
		if (j == Teban.you || j == Teban.enemy)
			turn = j;
		else {
			System.out.println("プレイヤーは先手か後手です" + j);
			System.exit(0);
		}
		if (t == human || t == computer)
			type = t;
		else {
			System.out.println("プレイヤーは人間かコンピュータでなければなりません" + t);
			System.exit(0);
		}
	}

	int getTurn() {
		return turn;
	}

	int getType() {
		return type;
	}

	Point Tactics(Board bd) {
		int random = 0;
		ArrayList<Point> point = new ArrayList<Point>();
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (bd.teban[i][j].getKomaType() == Teban.enemy)
					point.add(new Point(j, i));
			}
		}
		System.out.println(point);
		do {
			Random r = new Random(System.currentTimeMillis());
			random = r.nextInt(point.size());
		} while (random < 0);
		return point.get(random);
	}

	Point nextMove(Board bd, Point p) {
		if (type == human) {
			System.out.println("人間です");
			return p;
		} else if (type == computer)
			return Tactics(bd);
		return (new Point(-1, -1));
	}

}

class Teban {
	public final static int none = 0;
	public final static int you = 1;
	public final static int enemy = 2;
	private int komaType;

	Teban() {
		komaType = 0;
	}

	void setKomaType(int t) {
		if (t == you || t == enemy || t == none)
			komaType = t;
		else
			System.out.println("あなた、敵か無しのみです" + t);
	}

	int getKomaType() {
		return komaType;
	}

	void paint(Graphics g, Point p) {

		if (komaType == enemy) {
			g.setColor(Color.blue);
			g.fillRect(p.x, p.y, 10, 10);
		} else if (komaType == you) {
			g.setColor(Color.red);
			g.fillRect(p.x, p.y, 10, 10);
		}
	}
}

class Move {
	public final static int cannot = 0;
	public final static int can = 1;
	private int place;

	Move() {
		place = cannot;
	}

	int getPlace() {
		return place;
	}

	void setPlace(int p) {
		if (p == cannot || p == can)
			place = p;
		else
			System.out.println("できるかできないのみです");
	}

	void paint(Graphics g, Point p) {
		if (place == can) {
			g.setColor(Color.green);
			g.fillOval(p.x, p.y, 20, 20);
		}
	}
}

class Koma {
	public final static int none = 0;
	public final static int fu = 1;
	public final static int kyo = 2;
	public final static int kei = 3;
	public final static int silver = 4;
	public final static int gold = 5;
	public final static int hi = 6;
	public final static int kaku = 7;
	public final static int king = 8;
	public final static int gyoku = 9;
	public final static int evolution = 10;
	public final static int tokin = fu + evolution; // 11
	public final static int narikyo = kyo + evolution; // 12
	public final static int narikei = kei + evolution; // 13
	public final static int narigin = silver + evolution; // 14
	public final static int ryuou = hi + evolution; // 16
	public final static int ryuma = kaku + evolution; // 17

	private int obverse;

	Koma() {
		obverse = 0;
	}

	void paint(Graphics g, Point p, int s) {
		g.setColor(Color.black);
		if (s == 1) {
			Font font = new Font("HGP行書体", Font.BOLD, 50);
			g.setFont(font);
		} else if (s == 2) {
			Font font = new Font("HGP行書体", Font.BOLD, 30);
			g.setFont(font);
		}

		if (obverse == fu)
			g.drawString("歩", p.x, p.y);
		else if (obverse == kyo)
			g.drawString("香", p.x, p.y);
		else if (obverse == kei)
			g.drawString("桂", p.x, p.y);
		else if (obverse == silver)
			g.drawString("銀", p.x, p.y);
		else if (obverse == gold)
			g.drawString("金", p.x, p.y);
		else if (obverse == hi)
			g.drawString("飛", p.x, p.y);
		else if (obverse == kaku)
			g.drawString("角", p.x, p.y);
		else if (obverse == king)
			g.drawString("王", p.x, p.y);
		else if (obverse == gyoku)
			g.drawString("玉", p.x, p.y);
		else if (obverse == fu + evolution)
			g.drawString("と", p.x, p.y);
		else if (obverse == ryuou)
			g.drawString("龍", p.x, p.y);
		else if (obverse == ryuma)
			g.drawString("馬", p.x, p.y);
		else if (obverse == narikyo || obverse == narikei || obverse == narigin)
			g.drawString("金", p.x, p.y);
	}

	void setObverse(int name) {
		if (name == fu || name == kyo || name == kei || name == silver || name == gold || name == hi || name == kaku
				|| name == king || name == gyoku || name == tokin || name == narikyo || name == narikei || name == narigin
				|| name == ryuou || name == ryuma || name == none)
			obverse = name;
		else
			System.out.println("その駒はありません");
	}

	int getObverse() {
		return obverse;
	}

}

class Board {
	int unit_size = 70;
	Koma koma[][] = new Koma[9][9];
	Koma myKoma[] = new Koma[25];
	Koma enemyKoma[] = new Koma[25];
	Teban teban[][] = new Teban[9][9];
	Move move[][] = new Move[9][9];
	Point direction[] = new Point[12];

	Board() {
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				koma[i][j] = new Koma();
				teban[i][j] = new Teban();
				move[i][j] = new Move();
			}
		}

		for (int i = 0; i < 25; i++) {
			myKoma[i] = new Koma();
			enemyKoma[i] = new Koma();
		}
		// 敵の駒をセット

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				if (i == 2)
					koma[i][j].setObverse(Koma.fu);
				else if (i == 1 && j == 1)
					koma[i][j].setObverse(Koma.hi);
				else if (i == 1 && j == 7)
					koma[i][j].setObverse(Koma.kaku);
				else if (i == 0) {
					if (j == 0 || j == 8)
						koma[i][j].setObverse(Koma.kyo);
					else if (j == 1 || j == 7)
						koma[i][j].setObverse(Koma.kei);
					else if (j == 2 || j == 6)
						koma[i][j].setObverse(Koma.silver);
					else if (j == 3 || j == 5)
						koma[i][j].setObverse(Koma.gold);
					else if (j == 4)
						koma[i][j].setObverse(Koma.gyoku);
				}
			}
		}

		// 自分の駒をセット

		for (int i = 6; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (i == 6)
					koma[i][j].setObverse(Koma.fu);
				else if (i == 7 && j == 1)
					koma[i][j].setObverse(Koma.kaku);
				else if (i == 7 && j == 7)
					koma[i][j].setObverse(Koma.hi);
				else if (i == 8) {
					if (j == 0 || j == 8)
						koma[i][j].setObverse(Koma.kyo);
					else if (j == 1 || j == 7)
						koma[i][j].setObverse(Koma.kei);
					else if (j == 2 || j == 6)
						koma[i][j].setObverse(Koma.silver);
					else if (j == 3 || j == 5)
						koma[i][j].setObverse(Koma.gold);
					else if (j == 4)
						koma[i][j].setObverse(Koma.king);
				}
			}
		}

		// 敵の手番をセット

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				if (i == 0 || i == 2)
					teban[i][j].setKomaType(Teban.enemy);
				else if (i == 1 && (j == 1 || j == 7))
					teban[i][j].setKomaType(Teban.enemy);
			}
		}

		// 自分の手番をセット

		for (int i = 6; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (i == 6 || i == 8)
					teban[i][j].setKomaType(Teban.you);
				else if (i == 7 && (j == 1 || j == 7))
					teban[i][j].setKomaType(Teban.you);
			}
		}

		direction[0] = new Point(1, 0);
		direction[1] = new Point(1, -1);
		direction[2] = new Point(0, -1);
		direction[3] = new Point(-1, -1);
		direction[4] = new Point(-1, 0);
		direction[5] = new Point(-1, 1);
		direction[6] = new Point(0, 1);
		direction[7] = new Point(1, 1);
		direction[8] = new Point(1, -2); // 桂馬専用
		direction[9] = new Point(-1, -2); // 桂馬専用
		direction[10] = new Point(-1, 2); // 桂馬専用
		direction[11] = new Point(1, 2); // 桂馬専用
		printBoard();
	}

	void paint(Graphics g, int unit_size, ArrayList<Integer> my, ArrayList<Integer> enemy) {
		g.setColor(new Color(0, 128, 0));
		g.fillRect(0, 0, unit_size * 11, unit_size * 11);

		g.setColor(new Color(249, 194, 88));
		g.fillRect(unit_size, unit_size, unit_size * 9, unit_size * 9);

		g.setColor(Color.black);
		for (int i = 1; i <= 10; i++) {
			g.drawLine(unit_size, i * unit_size, 10 * unit_size, i * unit_size);
		}

		for (int i = 1; i <= 10; i++) {
			g.drawLine(i * unit_size, unit_size, i * unit_size, 10 * unit_size);
		}

		int x = 80;
		int y = 120;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Point p = new Point(x, y);
				koma[i][j].paint(g, p, 1);
				x += unit_size;
			}
			x = 80;
			y += unit_size;
		}

		x = 70;
		y = 70;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Point p = new Point(x, y);
				teban[i][j].paint(g, p);
				x += unit_size;
			}
			x = 70;
			y += unit_size;
		}

		x = 95;
		y = 90;
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				Point p = new Point(x, y);
				move[i][j].paint(g, p);
				x += unit_size;
			}
			x = 95;
			y += unit_size;
		}

		x = 0;
		y = 740;

		// 自分のとったコマの表示をする

		for (int i = 0; i < my.size(); i++) {
			myKoma[i].setObverse(my.get(i));
			Point p = new Point(x, y);
			myKoma[i].paint(g, p, 2);
			x += 30;
		}

		// 相手のとったコマを表示する

		x = 0;
		y = 50;
		for (int i = 0; i < enemy.size(); i++) {
			enemyKoma[i].setObverse(enemy.get(i));
			Point p = new Point(x, y);
			enemyKoma[i].paint(g, p, 2);
			x += 30;
		}

	}

	boolean isOnBoard(int x, int y) {
		if (x < 0 || x > 8 || y < 0 || y > 8)
			return false;
		return true;
	}

	ArrayList<Point> canMoveList(int x, int y, Point[] d) {
		ArrayList<Point> line = new ArrayList<Point>();
		int cx = 0;
		int cy = 0;
		if (teban[y][x].getKomaType() == Teban.enemy) {
			for (int i = 0; i < 12; i++) {
				d[i].y = -d[i].y;
			}
		}

		if (koma[y][x].getObverse() == Koma.fu) {
			cx = x + d[2].x;
			cy = y + d[2].y;
			if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType()) {
				line.add(new Point(cx, cy));
			}

		} else if (koma[y][x].getObverse() == Koma.kyo) {
			cx = x + d[2].x;
			cy = y + d[2].y;
			while (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType()) {
				line.add(new Point(cx, cy));
				if (teban[cy][cx].getKomaType() > 0)
					break;
				cx += d[2].x;
				cy += d[2].y;
			}

		} else if (koma[y][x].getObverse() == Koma.kei) {
			for (int i = 8; i <= 9; i++) {
				cx = x + d[i].x;
				cy = y + d[i].y;
				if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType())
					line.add(new Point(cx, cy));
				cx = x;
				cy = y;
			}

		} else if (koma[y][x].getObverse() == Koma.silver) {
			for (int i = 1; i <= 7; i++) {
				if (i == 4 || i == 6)
					continue;
				cx = x + d[i].x;
				cy = y + d[i].y;
				if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType())
					line.add(new Point(cx, cy));
				cx = x;
				cy = y;
			}

		} else if (koma[y][x].getObverse() == Koma.gold || koma[y][x].getObverse() == Koma.tokin
				|| koma[y][x].getObverse() == Koma.narikyo || koma[y][x].getObverse() == Koma.narikei
				|| koma[y][x].getObverse() == Koma.narigin) {
			for (int i = 0; i < 7; i++) {
				if (i == 5)
					continue;
				cx = x + d[i].x;
				cy = y + d[i].y;
				if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType())
					line.add(new Point(cx, cy));
				cx = x;
				cy = y;
			}

		} else if (koma[y][x].getObverse() == Koma.kaku) {
			for (int i = 1; i < 8; i += 2) {
				cx = x + d[i].x;
				cy = y + d[i].y;
				while (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType()) {
					line.add(new Point(cx, cy));
					if (teban[cy][cx].getKomaType() > 0)
						break;
					cx += d[i].x;
					cy += d[i].y;
					if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType()
							&& teban[cy][cx].getKomaType() > 0) {
						line.add(new Point(cx, cy));
						break;
					}
				}
				cx = x;
				cy = y;
			}

		} else if (koma[y][x].getObverse() == Koma.hi) {
			for (int i = 0; i < 7; i += 2) {
				cx = x + d[i].x;
				cy = y + d[i].y;
				while (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType()) {
					line.add(new Point(cx, cy));
					if (teban[cy][cx].getKomaType() > 0)
						break;
					cx += d[i].x;
					cy += d[i].y;
					if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType()
							&& teban[cy][cx].getKomaType() > 0) {
						line.add(new Point(cx, cy));
						break;
					}
				}
				cx = x;
				cy = y;
			}

		} else if (koma[y][x].getObverse() == Koma.king || koma[y][x].getObverse() == Koma.gyoku) {
			for (int i = 0; i < 8; i++) {
				cx = x + d[i].x;
				cy = y + d[i].y;
				if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType()) {
					line.add(new Point(cx, cy));
				}
				cx = x;
				cy = y;
			}

		} else if (koma[y][x].getObverse() == Koma.ryuou) {
			for (int i = 0; i < 8; i++) {
				cx = x + d[i].x;
				cy = y + d[i].y;
				if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType() && i % 2 == 1) {
					line.add(new Point(cx, cy));
				}
				while (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType() && i % 2 == 0) {
					line.add(new Point(cx, cy));
					if (teban[cy][cx].getKomaType() > 0)
						break;
					cx += d[i].x;
					cy += d[i].y;
					if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType()
							&& teban[cy][cx].getKomaType() > 0) {
						line.add(new Point(cx, cy));
						break;
					}
				}
				cx = x;
				cy = y;
			}
		} else if (koma[y][x].getObverse() == Koma.ryuma) {
			for (int i = 0; i < 8; i++) {
				cx = x + d[i].x;
				cy = y + d[i].y;
				if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType() && i % 2 == 0) {
					line.add(new Point(cx, cy));
				}
				while (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType() && i % 2 == 1) {
					line.add(new Point(cx, cy));
					if (teban[cy][cx].getKomaType() > 0)
						break;
					cx += d[i].x;
					cy += d[i].y;
					if (isOnBoard(cx, cy) && teban[y][x].getKomaType() != teban[cy][cx].getKomaType()
							&& teban[cy][cx].getKomaType() > 0) {
						line.add(new Point(cx, cy));
						break;
					}
				}
				cx = x;
				cy = y;
			}
		}
		if (teban[y][x].getKomaType() == Teban.enemy) {
			for (int i = 0; i < 12; i++) {
				d[i].y = -d[i].y;
			}
		}
		return line;
	}

	void printBoard() {
		System.out.println("盤面(Koma): ");
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.printf("%2d ", koma[i][j].getObverse());
			}
			System.out.println();
		}

		System.out.println("あなたと敵のコマ(Teban):");
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.printf("%2d ", teban[i][j].getKomaType());
			}
			System.out.println();
		}

		System.out.println("移動可能なマス目:(Move)");
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				System.out.printf("%2d ", move[i][j].getPlace());
			}
			System.out.println();
		}

		System.out.println("あなたのコマ:");
		for (int i = 0; i < 25; i++)
			System.out.printf("%d ", myKoma[i].getObverse());
		System.out.println("");

		System.out.println("対戦相手のコマ:");
		for (int i = 0; i < 25; i++)
			System.out.printf("%d ", enemyKoma[i].getObverse());
		System.out.println("");

	}

	void setKoma(int x, int y, int k) {
		koma[y][x].setObverse(k);
	}

	void setTeban(int x, int y, int t) {
		teban[y][x].setKomaType(t);
	}

	void setKomaAndTeban(Point oldPoint, Point newPoint, int k, int t) {
		setKoma(oldPoint.x, oldPoint.y, Koma.none);
		setKoma(newPoint.x, newPoint.y, k);
		setTeban(oldPoint.x, oldPoint.y, Teban.none);
		setTeban(newPoint.x, newPoint.y, t);
	}

	void setMove(int x, int y, int p) {
		move[y][x].setPlace(p);
	}

}

public class Shougi extends JPanel {

	public final static int UNIT_SIZE = 70;
	private Player[] player = new Player[2];
	ArrayList<Point> line = new ArrayList<Point>(); // クリックした時のコマの移動可能なマス目のリスト
	ArrayList<Integer> myKoma = new ArrayList<Integer>(); // 自分のコマのリスト
	ArrayList<Integer> enemyKoma = new ArrayList<Integer>(); // 相手のコマのリスト
	Point gp = new Point(); // 盤面上のクリックしたコマの位置
	Point gp3 = new Point(); // 盤面外(とったコマ)のコマの位置
	Board board = new Board();
	private int turn;

	public Shougi() {
		setPreferredSize(new Dimension(UNIT_SIZE * 11, UNIT_SIZE * 11));
		addMouseListener(new MouseProc());
		player[0] = new Player(Teban.you, Player.human);
		player[1] = new Player(Teban.enemy, Player.computer);
		turn = Teban.you;
	}

	public void paintComponent(Graphics g) {
		board.paint(g, UNIT_SIZE, myKoma, enemyKoma);
	}

	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.getContentPane().setLayout(new FlowLayout());
		f.getContentPane().add(new Shougi());
		f.pack();
		f.setResizable(false);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}

	void EndMessageDialog(String str) {
		JOptionPane.showMessageDialog(this, str, "ゲーム終了", JOptionPane.INFORMATION_MESSAGE);
		System.exit(0);
	}

	void attentionMessage(String str) {
		JOptionPane.showMessageDialog(this, str, "注意してください!!!", JOptionPane.INFORMATION_MESSAGE);
	}

	void evolutionMessage(Point p) {
		JFrame frame = new JFrame("成りますか？？？？？");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(300, 200);
		frame.setLocationRelativeTo(null);
		frame.setLayout(new FlowLayout());

		// ボタン1を作成
		JButton button1 = new JButton("成ります");
		JButton button2 = new JButton("成りません");

		frame.add(button1);
		frame.add(button2);

		frame.setVisible(true);

		// ボタンを押した時の処理を設定
		button1.addActionListener(e -> {
			board.setKoma(p.x, p.y, board.koma[p.y][p.x].getObverse() + Koma.evolution);
			repaint();
			board.printBoard();
			if (player[turn - 1].getType() == Player.computer) {
				Thread th = new TacticsThread();
				th.start();
			}
			frame.setVisible(false);
		});

		button2.addActionListener(e -> {
			repaint();
			if (player[turn - 1].getType() == Player.computer) {
				Thread th = new TacticsThread();
				th.start();
			}
			frame.setVisible(false);
		});

	}

	void changeTurn() {
		if (turn == Teban.you)
			turn = Teban.enemy;
		else
			turn = Teban.you;
	}

	class MouseProc extends MouseAdapter {
		public void mouseClicked(MouseEvent me) {
			Point point = me.getPoint();
			gp.x = point.x / UNIT_SIZE - 1;
			gp.y = point.y / UNIT_SIZE - 1;
			System.out.println(gp.x + "," + gp.y);
			if (!board.isOnBoard(gp.x, gp.y)) {
				System.out.println("ボード外です");
				removeMouseListener(this);
				addMouseListener(new MouseProc2());
				return;
			}

			if (turn == Teban.you && board.teban[gp.y][gp.x].getKomaType() == Teban.enemy) {
				attentionMessage("対戦相手の番ではありません");
				return;
			} else if (turn == Teban.enemy && board.teban[gp.y][gp.x].getKomaType() == Teban.you) {
				attentionMessage("あなたの番ではありません");
				return;
			}

			line = board.canMoveList(gp.x, gp.y, board.direction); // クリックした駒の移動可能なマスを取得

			for (int i = 0; i < line.size(); i++) {
				board.move[line.get(i).y][line.get(i).x].setPlace(Move.can);
			}
			repaint();
			board.printBoard();
			removeMouseListener(this);
			addMouseListener(new MouseProc1());
		}
	}

	class MouseProc1 extends MouseAdapter {
		public void mouseClicked(MouseEvent me) {
			for (int i = 0; i < line.size(); i++) {
				board.move[line.get(i).y][line.get(i).x].setPlace(Move.cannot);
			}
			Point point = me.getPoint();
			Point gp2 = new Point();
			int flag = 0;
			gp2.x = point.x / UNIT_SIZE - 1;
			gp2.y = point.y / UNIT_SIZE - 1;

			if (!board.isOnBoard(gp2.x, gp2.y)) {
				removeMouseListener(this);
				addMouseListener(new MouseProc());
				return;
			}

			for (int i = 0; i < line.size(); i++) {
				if (line.get(i).x == gp2.x && line.get(i).y == gp2.y) {
					changeTurn();
					if (board.koma[gp2.y][gp2.x].getObverse() == Koma.gyoku) {
						board.setKomaAndTeban(gp, gp2, board.koma[gp.y][gp.x].getObverse(), Teban.you);
						repaint();
						EndMessageDialog("あなたの勝ちです");
					} else if (board.koma[gp2.y][gp2.x].getObverse() == Koma.king) {
						board.setKomaAndTeban(gp, gp2, board.koma[gp.y][gp.x].getObverse(), Teban.enemy);
						repaint();
						EndMessageDialog("対戦相手の勝ちです");
					}
					if (board.teban[gp.y][gp.x].getKomaType() == Teban.you) {
						if (board.teban[gp2.y][gp2.x].getKomaType() == Teban.enemy) {

							// コマをとる
							if (board.koma[gp2.y][gp2.x].getObverse() > Koma.evolution)
								myKoma.add(board.koma[gp2.y][gp2.x].getObverse() - Koma.evolution);
							else
								myKoma.add(board.koma[gp2.y][gp2.x].getObverse());
						}
						if (gp2.y < 3 && board.koma[gp.y][gp.x].getObverse() < Koma.evolution) {
							evolutionMessage(gp2);
							flag = 1;
						}

						board.setKomaAndTeban(gp, gp2, board.koma[gp.y][gp.x].getObverse(), Teban.you);
					} else if (board.teban[gp.y][gp.x].getKomaType() == Teban.enemy) {
						if (board.teban[gp2.y][gp2.x].getKomaType() == Teban.you) {

							// コマをとる
							if (board.koma[gp2.y][gp2.x].getObverse() > Koma.evolution)
								enemyKoma.add(board.koma[gp2.y][gp2.x].getObverse() - Koma.evolution);
							else
								enemyKoma.add(board.koma[gp2.y][gp2.x].getObverse());
						}
						if (gp2.y > 5 && board.koma[gp.y][gp.x].getObverse() < Koma.evolution) {
							evolutionMessage(gp2);
						}
						board.setKomaAndTeban(gp, gp2, board.koma[gp.y][gp.x].getObverse(), Teban.enemy);
					}
					break;
				}
			}
			Collections.sort(enemyKoma, Collections.reverseOrder());
			Collections.sort(myKoma, Collections.reverseOrder());
			repaint();
			board.printBoard();
			removeMouseListener(this);
			if (player[turn - 1].getType() == Player.computer && flag == 0) {
				Thread th = new TacticsThread();
				th.start();
			}
			addMouseListener(new MouseProc());
		}
	}

	class MouseProc2 extends MouseAdapter {
		public void mouseClicked(MouseEvent me) {
			Point point = me.getPoint();
			gp3.x = point.x / 30;
			gp3.y = point.y / UNIT_SIZE - 1;
			System.out.println(gp3.x + "," + gp3.y);

			// クリックしたのが相手のコマだった場合

			if (gp3.y == -1) {
				if (board.enemyKoma[gp3.x].getObverse() != Koma.none) {
					ArrayList<Integer> stack_j = new ArrayList<Integer>(); // 歩がある列のリスト
					for (int i = 0; i < 9; i++) {
						if (enemyKoma.get(gp3.x) == Koma.kei && (i == 7 || i == 8))
							continue;
						if ((enemyKoma.get(gp3.x) == Koma.fu || enemyKoma.get(gp3.x) == Koma.kyo) && (i == 8))
							continue;
						for (int j = 0; j < 9; j++) {
							if (board.koma[i][j].getObverse() == Koma.none)
								board.move[i][j].setPlace(Move.can);
							if (board.koma[i][j].getObverse() == Koma.fu && board.teban[i][j].getKomaType() == Teban.enemy
									&& enemyKoma.get(gp3.x) == Koma.fu)
								stack_j.add(j);
							if (board.koma[i][j].getObverse() == Koma.king && enemyKoma.get(gp3.x) == Koma.fu) {
								System.out.println("歩王手はできません");
								board.move[i - 1][j].setPlace(Move.cannot);
							}
						}
					}
					for (int i = 0; i < stack_j.size(); i++) {
						for (int j = 0; j < 9; j++) {
							board.move[j][stack_j.get(i)].setPlace(Move.cannot);
						}
					}
				}
			}

			// クリックしたのが自分のコマだった場合

			if (gp3.y == 9) {
				if (board.myKoma[gp3.x].getObverse() != Koma.none) {
					ArrayList<Integer> stack_j = new ArrayList<Integer>(); // 歩がある列のリスト
					for (int i = 0; i < 9; i++) {
						if (myKoma.get(gp3.x) == Koma.kei && (i == 0 || i == 1))
							continue;
						if ((myKoma.get(gp3.x) == Koma.fu || myKoma.get(gp3.x) == Koma.kyo) && (i == 0))
							continue;
						for (int j = 0; j < 9; j++) {
							if (board.koma[i][j].getObverse() == Koma.none)
								board.move[i][j].setPlace(Move.can);
							if (board.koma[i][j].getObverse() == Koma.fu && board.teban[i][j].getKomaType() == Teban.you
									&& myKoma.get(gp3.x) == Koma.fu)
								stack_j.add(j);
							if (board.koma[i][j].getObverse() == Koma.gyoku && myKoma.get(gp3.x) == Koma.fu) {
								System.out.println("歩王手はできません");
								board.move[i + 1][j].setPlace(Move.cannot);
							}
						}
					}
					for (int i = 0; i < stack_j.size(); i++) {
						for (int j = 0; j < 9; j++) {
							board.move[j][stack_j.get(i)].setPlace(Move.cannot);
						}
					}
				}
			}
			repaint();
			removeMouseListener(this);
			addMouseListener(new MouseProc3());
		}
	}

	class MouseProc3 extends MouseAdapter {
		public void mouseClicked(MouseEvent me) {
			Point point = me.getPoint();
			Point gp4 = new Point();
			System.out.println("gp3:" + gp3.x + "," + gp3.y);
			gp4.x = point.x / UNIT_SIZE - 1;
			gp4.y = point.y / UNIT_SIZE - 1;

			if (!board.isOnBoard(gp4.x, gp4.y)) {
				removeMouseListener(this);
				addMouseListener(new MouseProc());
				return;
			}
			if (gp3.y == -1) {
				if (board.move[gp4.y][gp4.x].getPlace() == Move.can) {
					board.setKoma(gp4.x, gp4.y, enemyKoma.get(gp3.x));
					board.setTeban(gp4.x, gp4.y, Teban.enemy);
					enemyKoma.set(gp3.x, Koma.none);
				}
			} else if (gp3.y == 9) {
				if (board.move[gp4.y][gp4.x].getPlace() == Move.can) {
					board.setKoma(gp4.x, gp4.y, myKoma.get(gp3.x));
					board.setTeban(gp4.x, gp4.y, Teban.you);
					myKoma.set(gp3.x, Koma.none);
				}
			}

			// Moveを元に戻す
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (board.move[i][j].getPlace() == Move.can)
						board.move[i][j].setPlace(Move.cannot);
				}
			}

			changeTurn();
			Collections.sort(enemyKoma, Collections.reverseOrder());
			Collections.sort(myKoma, Collections.reverseOrder());
			repaint();
			board.printBoard();
			removeMouseListener(this);
			if (player[turn - 1].getType() == Player.computer) {
				Thread th = new TacticsThread();
				th.start();
			}

			addMouseListener(new MouseProc());
		}
	}

	class TacticsThread extends Thread {
		public void run() {
			try {
				Thread.sleep(2000);
				ArrayList<Point> Setable = new ArrayList<Point>();
				Random r = new Random(System.currentTimeMillis());
				int rand = 0;
				if (enemyKoma.size() > 0) {
					System.out.println("enemyKoma.size()=" + enemyKoma.size());
					if (board.enemyKoma[rand].getObverse() != Koma.none) {
						ArrayList<Integer> stack_j = new ArrayList<Integer>(); // 歩がある列のリスト
						for (int i = 0; i < 9; i++) {
							if (enemyKoma.get(rand) == Koma.kei && (i == 7 || i == 8))
								continue;
							if ((enemyKoma.get(rand) == Koma.fu || enemyKoma.get(rand) == Koma.kyo
									|| enemyKoma.get(rand) == Koma.silver) && (i == 8))
								continue;
							for (int j = 0; j < 9; j++) {
								if (board.koma[i][j].getObverse() == Koma.none) {
									board.move[i][j].setPlace(Move.can);
									System.out.println("セットしました" + i + "," + j);
								}
								if (board.koma[i][j].getObverse() == Koma.fu && board.teban[i][j].getKomaType() == Teban.enemy
										&& enemyKoma.get(rand) == Koma.fu)
									stack_j.add(j);
								if (board.koma[i][j].getObverse() == Koma.king && enemyKoma.get(rand) == Koma.fu) {
									board.move[i - 1][j].setPlace(Move.cannot);
									System.out.println("歩で王手はセットできません" + i + "," + j);
								}
							}
						}
						for (int i = 0; i < stack_j.size(); i++) {
							for (int j = 0; j < 9; j++) {
								board.move[j][stack_j.get(i)].setPlace(Move.cannot);
								System.out.println("セットできません" + i + "," + j);
							}
						}
					}
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							if (board.move[i][j].getPlace() == Move.can)
								Setable.add(new Point(j, i));
						}
					}
				}

				System.out.println("Setable: " + Setable);

				if (Setable.size() == 0 || enemyKoma.size() == 0) {
					ArrayList<Point> cpList = new ArrayList<Point>();
					Point oldPoint = new Point();
					Point newPoint = new Point();
					int random = 0;
					System.out.println("turn:" + turn);
					do {
						oldPoint = player[turn - 1].nextMove(board, new Point(-1, -1));
						System.out.println(oldPoint);
						cpList = board.canMoveList(oldPoint.x, oldPoint.y, board.direction);
					} while (cpList.size() <= 0);
					System.out.println("oldPoint:" + oldPoint);
					System.out.println(cpList);
					r = new Random(System.currentTimeMillis());

					random = r.nextInt(cpList.size());
					System.out.println("random" + random);

					newPoint = cpList.get(random);
					System.out.println("oldPoint:" + oldPoint);
					System.out.println("newPoint:" + newPoint);
					if (board.teban[newPoint.y][newPoint.x].getKomaType() == Teban.you) {
						if (board.koma[newPoint.y][newPoint.x].getObverse() > Koma.evolution)
							enemyKoma.add(board.koma[newPoint.y][newPoint.x].getObverse() - Koma.evolution);
						else
							enemyKoma.add(board.koma[newPoint.y][newPoint.x].getObverse());
					}
					if (board.koma[newPoint.y][newPoint.x].getObverse() == Koma.king)
						EndMessageDialog("対戦相手の勝ちです");
					board.setKomaAndTeban(oldPoint, newPoint, board.koma[oldPoint.y][oldPoint.x].getObverse(), Teban.enemy);
				} else {
					int random = r.nextInt(Setable.size());
					System.out.println("Setable.size()=" + Setable.size());
					System.out.println("rand=" + rand);
					System.out.println("x=" + Setable.get(random).x + " y=" + Setable.get(random).y + " enemyKoma.get(rand)="
							+ enemyKoma.get(rand));
					board.setTeban(Setable.get(random).x, Setable.get(random).y, Teban.enemy);
					board.setKoma(Setable.get(random).x, Setable.get(random).y, enemyKoma.get(rand));
					enemyKoma.set(rand, Koma.none);
				}

				for (int i = 0; i < 9; i++) {
					for (int j = 0; j < 9; j++) {
						if (board.move[i][j].getPlace() == Move.can)
							board.move[i][j].setPlace(Move.cannot);
					}
				}
				Collections.sort(enemyKoma, Collections.reverseOrder());
				changeTurn();
				repaint();
				board.printBoard();
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}

}
