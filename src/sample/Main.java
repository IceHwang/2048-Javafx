package sample;


import javafx.animation.FadeTransition;
import javafx.animation.PathTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


public class Main extends Application
{
    Card[][] cards=new Card[4][4];
    Card[][] animCards=new Card[4][4];
    LinkedList<Point> emptyPoints=new LinkedList<>();
    private final int m_left = 0, m_right = 1, m_up = 2, m_down = 3;

    //status为false时没有动画正在移动，为ture时可能有动画正在移动
    boolean moveStatus=false;


    @Override
    public void start(Stage primaryStage) throws Exception{
        StackPane stackPane =new StackPane();
        GridPane backgroundPane=new GridPane();
        backgroundPane.setStyle("-fx-background-color: #b2ada0");

        GridPane cardsPane=new GridPane();

        GridPane animPane=new GridPane();

        stackPane.setAlignment(Pos.CENTER);
        stackPane.getChildren().add(backgroundPane);
        stackPane.getChildren().add(cardsPane);
        stackPane.getChildren().add(animPane);

        stackPane.setOnKeyPressed(event ->
        {
            switch (event.getCode())
            {
                case LEFT:
                    move(m_left);
                    break;
                case UP:
                    move(m_up);
                    break;
                case RIGHT:
                    move(m_right);
                    break;
                case DOWN:
                    move(m_down);
                    break;
                default:
                    break;
            }
        });

        gamestart();
        for(int x=0;x<4;x++)
        {
            for (int y=0;y<4;y++)
            {
                Card card=new Card();
                GridPane.setMargin(card,new Insets(5));
                cardsPane.add(card,x,y);
            }
        }
        for(int x=0;x<4;x++)
        {
            for (int y=0;y<4;y++)
            {
                GridPane.setMargin(cards[x][y],new Insets(5));
                cardsPane.add(cards[x][y],x,y);
            }
        }
        for(int x=0;x<4;x++)
        {
            for (int y=0;y<4;y++)
            {
                animPane.setMargin(animCards[x][y],new Insets(5));
                animCards[x][y].setVisible(false);
                cardsPane.add(animCards[x][y],x,y);
            }
        }

        Scene scene =new Scene(stackPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("2048");
        primaryStage.setResizable(false);
        primaryStage.show();
        stackPane.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }


    public void gamestart() {
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                cards[x][y]=new Card();
            }
        }
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                animCards[x][y]=new Card();
            }
        }

        addRandomNum();
        addRandomNum();


    }

    private boolean addRandomNum() {
        emptyPoints.clear();
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                if (cards[x][y].getNum() == 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }


        Point point = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));

        cards[point.x][point.y].setNum(Math.random() > 0.1 ? 2 : 4);
        addCardAnim(point.x,point.y);


        if (emptyPoints.size() == 0) {

            return false;
        }


        return true;

    }

    private void move(int direc)
    {

        if(moveStatus==true)
            return;
        moveStatus=true;

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                moveStatus=false;
            }
        }, 200);
        timer=null;

        boolean flag=false;

        switch (direc) {
            case m_left:
                flag = moveLeft();
                break;
            case m_right:
                flag = moveRight();
                break;
            case m_up:
                flag = moveUp();
                break;
            case m_down:
                flag = moveDown();
                break;
        }

        if (flag == true)//移动成功
        {
//            MainActivity.getMainActivity().withdraw_flag = true;
//            for (int x = 0; x < 4; x++) {
//                for (int y = 0; y < 4; y++) {
//                    pre_cards[x][y].setNum(tmp_cards[x][y].getNum());
//                }
//            }
//            pre_score = tmp_score;




            if (addRandomNum() == false)//添加卡片并验证游戏是否结束,,,,,,逻辑错误,无法验证
            {
                checkgameover();
            }

            //MainActivity.getMainActivity().encoder(cards);



        } else {
            //移动失败,没有可以移动的卡片
        }
    }

    private void checkgameover()//在卡片填满后检验还能否消去
    {
        for (int y = 0; y < 4; y++) {
            for (int x = 1; x < 4; x++) {
                if (cards[x - 1][y].getNum() == cards[x][y].getNum()) {
                    return;//可消去
                }
            }
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 1; y < 4; y++) {
                if (cards[x][y - 1].getNum() == cards[x][y].getNum()) {
                    return;//可消去
                }
            }
        }

        //不可消去
        gameover();
    }

    private void gameover()
    {
        Stage stage=new Stage();
        VBox vBox=new VBox();
        Label label=new Label("GAME OVER");
        Button button =new Button("OK");
        vBox.getChildren().addAll(label,button);
        VBox.setMargin(label,new Insets(80,80,50,80));
        VBox.setMargin(button,new Insets(0,80,30,100));
        stage.setScene(new Scene(vBox));
        stage.show();
        stage.setResizable(false);
        button.setOnAction(event ->
        {
            stage.close();
            for (int i = 0; i < 4; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    cards[i][j].setNum(0);
                    animCards[i][j].setNum(0);
                }
            }
            addRandomNum();
            addRandomNum();
        });
    }

    private boolean moveLeft()
    {
        boolean flag=false;

        for(int y=0;y<4;y++)
        {
            for(int x=1;x<4;x++)
            {
                if(cards[x][y].getNum()==0)//不是可移动的卡片
                    continue;

                for(int xx=x-1;xx>=-1;xx--)
                {
                    if(xx==-1)//移动到边际
                    {
                        xx++;

                        moveAnim(x,xx,y,y);

                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

                        //Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
                        //animator.setTarget(cards[xx][y]);
                        //animator.setStartDelay(250);
                        //animator.start();

                        flag=true;
                        break;
                    }
                    else if(cards[xx][y].getNum()==0)//移动到空格
                    {
                        continue;
                    }
                    else if(cards[xx][y].equals(cards[x][y]))//移动到发生消除
                    {


                        moveAnim(x,xx,y,y);

                        cards[xx][y].setNum(cards[x][y].getNum()*2);
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[xx][y]);
//                        animator.setStartDelay(250);
//                        animator.start();

                        flag=true;
                        break;
                    }
                    else //移动到遇到不同卡片
                    {
                        xx++;

                        if(xx==x)
                            break;


                        moveAnim(x,xx,y,y);

                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[xx][y]);
//                        animator.setStartDelay(250);
//                        animator.start();

                        flag=true;
                        break;
                    }


                }

            }

        }


        return flag;

    }

    private boolean moveRight()
    {
        boolean flag=false;

        for(int y=0;y<4;y++)
        {
            for(int x=2;x>=0;x--)
            {
                if(cards[x][y].getNum()==0)//不是可移动的卡片
                    continue;

                for(int xx=x+1;xx<=4;xx++)
                {
                    if(xx==4)//移动到边际
                    {
                        xx--;

                        moveAnim(x,xx,y,y);
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[xx][y]);
//                        animator.setStartDelay(250);
//                        animator.start();
                        flag=true;
                        break;
                    }
                    else if(cards[xx][y].getNum()==0)//移动到空格
                    {
                        continue;
                    }
                    else if(cards[xx][y].equals(cards[x][y]))//移动到发生消除
                    {
                        //MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);

                        moveAnim(x,xx,y,y);
                        cards[xx][y].setNum(cards[x][y].getNum()*2);
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[xx][y]);
//                        animator.setStartDelay(250);
//                        animator.start();

                        flag=true;
                        break;
                    }
                    else //移动到遇到不同卡片
                    {
                        xx--;

                        if(xx==x)
                            break;


                        moveAnim(x,xx,y,y);
                        cards[xx][y].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[xx][y]);
//                        animator.setStartDelay(250);
//                        animator.start();

                        flag=true;
                        break;
                    }


                }

            }

        }


        return flag;

    }

    private boolean moveUp()
    {
        boolean flag=false;

        for(int x=0;x<4;x++)
        {
            for(int y=1;y<4;y++)
            {
                if(cards[x][y].getNum()==0)//不是可移动的卡片
                    continue;

                for(int yy=y-1;yy>=-1;yy--)
                {
                    if(yy==-1)//移动到边际
                    {
                        yy++;

                        moveAnim(x,x,y,yy);
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[x][yy]);
//                        animator.setStartDelay(250);
//                        animator.start();
                        flag=true;
                        break;
                    }
                    else if(cards[x][yy].getNum()==0)//移动到空格
                    {
                        continue;
                    }
                    else if(cards[x][yy].equals(cards[x][y]))//移动到发生消除
                    {
                        //MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);

                        moveAnim(x,x,y,yy);
                        cards[x][yy].setNum(cards[x][y].getNum()*2);
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[x][yy]);
//                        animator.setStartDelay(250);
//                        animator.start();

                        flag=true;
                        break;
                    }
                    else //移动到遇到不同卡片
                    {
                        yy++;

                        if(yy==y)
                            break;


                        moveAnim(x,x,y,yy);
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[x][yy]);
//                        animator.setStartDelay(250);
//                        animator.start();

                        flag=true;
                        break;
                    }


                }

            }

        }


        return flag;

    }

    private boolean moveDown()
    {
        boolean flag=false;

        for(int x=0;x<4;x++)
        {
            for(int y=2;y>=0;y--)
            {
                if(cards[x][y].getNum()==0)//不是可移动的卡片
                    continue;

                for(int yy=y+1;yy<=4;yy++)
                {
                    if(yy==4)//移动到边际
                    {
                        yy--;

                        moveAnim(x,x,y,yy);
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[x][yy]);
//                        animator.setStartDelay(250);
//                        animator.start();
                        flag=true;
                        break;
                    }
                    else if(cards[x][yy].getNum()==0)//移动到空格
                    {
                        continue;
                    }
                    else if(cards[x][yy].equals(cards[x][y]))//移动到发生消除
                    {
                        //MainActivity.getMainActivity().addScore(cards[x][y].getNum()*2);

                       moveAnim(x,x,y,yy);
                        cards[x][yy].setNum(cards[x][y].getNum()*2);
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[x][yy]);
//                        animator.setStartDelay(250);
//                        animator.start();

                        flag=true;
                        break;
                    }
                    else //移动到遇到不同卡片
                    {
                        yy--;

                        if(yy==y)
                            break;


                        moveAnim(x,x,y,yy);
                        cards[x][yy].setNum(cards[x][y].getNum());
                        cards[x][y].setNum(0);

//                        Animator animator = AnimatorInflater.loadAnimator(getContext(), R.animator.num_up);
//                        animator.setTarget(cards[x][yy]);
//                        animator.setStartDelay(250);
//                        animator.start();

                        flag=true;
                        break;
                    }


                }

            }

        }


        return flag;

    }



    public void moveAnim(final int from_x, final int to_x, final int from_y, final int to_y)
    {
        double width=Card.width;
        animCards[from_x][from_y].setNum(cards[from_x][from_y].getNum());
        animCards[from_x][from_y].setVisible(true);
        PathTransition anim=new PathTransition();
        anim.setDuration(Duration.millis(150));
        anim.setPath(new Line(0.5*width,0.5*width,
                (to_x-from_x+0.5)*width,(to_y-from_y+0.5)*width));
        anim.setNode(animCards[from_x][from_y]);
        anim.play();

        anim.setOnFinished(event -> {
            animCards[from_x][from_y].setVisible(false);
        });

    }

    public void addCardAnim(int x,int y)
    {
        FadeTransition fadeTransition1=new FadeTransition(Duration.millis(300));
        fadeTransition1.setFromValue(0.0f);
        fadeTransition1.setToValue(0.0f);
        fadeTransition1.setNode(cards[x][y]);
        fadeTransition1.play();

        FadeTransition fadeTransition2=new FadeTransition(Duration.millis(300));
        fadeTransition2.setFromValue(0.0f);
        fadeTransition2.setToValue(1.0f);
        fadeTransition2.setNode(cards[x][y]);

        fadeTransition1.setOnFinished(event -> fadeTransition2.play());
    }





}
