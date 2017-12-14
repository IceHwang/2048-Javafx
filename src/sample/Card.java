package sample;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Card extends ImageView
{


    int num=0;

    final static int width=100;
    final static Image[] images=
    {
            new Image("image/0.png"),
            new Image("image/2.png"),
            new Image("image/4.png"),
            new Image("image/8.png"),
            new Image("image/16.png"),
            new Image("image/32.png"),
            new Image("image/64.png"),
            new Image("image/128.png"),
            new Image("image/256.png"),
            new Image("image/512.png"),
            new Image("image/1024.png"),
            new Image("image/2048.png"),
            };


    public Card()
    {
        super();
        setFitHeight(width);
        setFitWidth(width);
        setNum(0);
    }

    public void setNum(int num)
    {
        this.num = num;
        switch (num)
        {
            case 0:
                this.setImage(images[0]);
                break;
            case 2:
                this.setImage(images[1]);
                break;
            case 4:
                this.setImage(images[2]);
                break;
            case 8:
                this.setImage(images[3]);
                break;
            case 16:
                this.setImage(images[4]);
                break;
            case 32:
                this.setImage(images[5]);
                break;
            case 64:
                this.setImage(images[6]);
                break;
            case 128:
                this.setImage(images[7]);
                break;
            case 256:
                this.setImage(images[8]);
                break;
            case 512:
                this.setImage(images[9]);
                break;
            case 1024:
                this.setImage(images[10]);
                break;
            case 2048:
                Main.example.youwin();
                this.setImage(images[11]);
                break;
            default:
                this.setImage(images[0]);
        }

    }

    public int getNum()
    {
        return num;
    }

    @Override
    public boolean equals(Object obj)
    {
        return this.num==((Card)obj).num;
    }
}
