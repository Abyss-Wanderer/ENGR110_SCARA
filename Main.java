/* Code for Assignment ?? 
 * Name:
 * Usercode:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;
import java.awt.*;

/** <description of class Main>
 */
public class Main{

    private Arm arm;
    private Drawing drawing;
    private ToolPath tool_path;
    public boolean pen;
    // state of the GUI
    private int state; // 0 - nothing
    // 1 - inverse point kinematics - point
    // 2 - enter path. Each click adds point  
    // 3 - enter path pause. Click does not add the point to the path

    /**      */
    public Main(){
        UI.initialise();
        UI.addButton("xy to angles", this::inverse);
        UI.addButton("Enter path XY", this::enter_path_xy);
        UI.addButton("Save path XY", this::save_xy);
        UI.addButton("Load path XY", this::load_xy);
        UI.addButton("Save path Ang", this::save_ang);
        UI.addButton("Load path Ang:Play", this::load_ang);        
        UI.addButton("Save path PWM", this::save_pwm);
        UI.addButton("Load path PWM:Play", this::load_pwm);
        UI.addButton("Line", this::drawHorizontalLine);
        UI.addButton("Circle", this::drawCircle);
        UI.addButton("Square", this::drawSquare);
        UI.addButton("Skynet", this::drawSkynet);
        UI.addButton("Snowman", ()->{drawing.changeShowSnowman();});

        UI.addButton("Quit", UI::quit);
        UI.setMouseMotionListener(this::doMouse);
        UI.setKeyListener(this::doKeys);

        this.tool_path = new ToolPath();
        this.arm = new Arm();
        this.drawing = new Drawing();
        this.run();
        arm.draw();

    }

    public void doKeys(String action){
        UI.printf("Key :%s \n", action);
        if (action.equals("b")) {
            // break - stop entering the lines
            state = 3;
            //

        } 
        else if (action.equals("1")) {
            if(state == 2) state = 3;
            if(state == 3) state = 2;
        } 
        else if (action.equals("2")) {
            state = 2;
        }

    }

    public void doMouse(String action, double x, double y) {
        //UI.printf("Mouse Click:%s, state:%d  x:%3.1f  y:%3.1f\n",
        //   action,state,x,y);
        UI.clearGraphics();
        String out_str=String.format("%3.1f %3.1f",x,y);
        UI.drawString(out_str, x+10,y+10);
        // 
        if ((state == 1)&&(action.equals("clicked"))){
            // draw as 

            arm.inverseKinematic(x,y);
            arm.draw();
            return;
        }

        if ( ((state == 2)||(state == 3))&&action.equals("moved") ){
            // draw arm and path
            arm.inverseKinematic(x,y);
            arm.draw();

            // draw segment from last entered point to current mouse position
            if ((state == 2)&&(drawing.get_path_size()>0)){
                PointXY lp = new PointXY();
                lp = drawing.get_path_last_point();
                if (lp.get_pen()){
                    UI.setColor(Color.GRAY);
                    UI.drawLine(lp.get_x(),lp.get_y(),x,y);
                }
            }
            drawing.draw();
        }

        // add point
        if ((state == 2) &&(action.equals("clicked"))){
            // add point(pen down) and draw
            UI.printf("Adding point x=%f y=%f\n",x,y);
            drawing.add_point_to_path(x,y,true); // add point with pen down

            arm.inverseKinematic(x,y);
            arm.draw();
            drawing.draw();
            drawing.print_path();
        }

        if ((state == 3) &&(action.equals("clicked"))){
            // add point and draw
            //UI.printf("Adding point x=%f y=%f\n",x,y);
            drawing.add_point_to_path(x,y,false); // add point wit pen up

            arm.inverseKinematic(x,y);
            arm.draw();
            drawing.draw();
            drawing.print_path();
            //state = 2;
        }

        if(state == 2 && (action.equals("dragged"))){            
            drawing.add_point_to_path(x,y,true); // add point wit pen up

            arm.inverseKinematic(x,y);
            arm.draw();
            drawing.draw();
            drawing.print_path();
        }
    }
    
    public void drawHorizontalLine(){
        drawing.add_point_to_path(220,155,false); // add point with pen down //220,160
        arm.inverseKinematic(260, 155); // 214,160
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(260,155,true); // add point with pen down //260, 160
        arm.inverseKinematic(214, 155); //214, 160
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(300,155,true); // add point with pen down // 300,160
        arm.inverseKinematic(214, 155); // 214, 160
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(340,155,true); // add point with pen down // 340,160
        arm.inverseKinematic(214, 155); // 214, 160
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(380,155,true); // add point with pen down // 380,160
        arm.inverseKinematic(214, 155); // 214, 160
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(420,155,true); // add point with pen down // 420, 160
        arm.inverseKinematic(214, 155); // 214, 160
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(450,155,true); // add point with pen down // 450, 160
        arm.inverseKinematic(450, 155); // 450, 160 
        arm.draw();
        drawing.draw();
        drawing.print_path();
    }
    
//    public void drawHorizontalLine(){
//        drawing.add_point_to_path(220,210,false); // add point with pen down //220,210
//        arm.inverseKinematic(214, 210); //214,2
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(260,210,true); // add point with pen down //260, 210
//        arm.inverseKinematic(214, 210);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(300,210,true); // add point with pen down
//        arm.inverseKinematic(214, 210);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(340,210,true); // add point with pen down
//        arm.inverseKinematic(214, 210);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(380,210,true); // add point with pen down
//        arm.inverseKinematic(214, 210);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(420,210,true); // add point with pen down
//        arm.inverseKinematic(214, 210);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(450,210,true); // add point with pen down
//        arm.inverseKinematic(450, 210);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//    }
    
//    public void drawSquare(){        
//        drawing.add_point_to_path(300,140,false); // add point with pen down //70
//        arm.inverseKinematic(300, 140);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(380,140,true); // add point with pen down //70
//        arm.inverseKinematic(380, 140);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(380,200,true); // add point with pen down //130
//        arm.inverseKinematic(380,200);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(300,200,true); // add point with pen down
//        arm.inverseKinematic(300,200);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(300,140,true); // add point with pen down
//        arm.inverseKinematic(300,140);
//        UI.clearGraphics();
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//    }

    public void drawSquare(){        
        drawing.add_point_to_path(360,120,false); // add point with pen down
        arm.inverseKinematic(360, 120);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(420,120,true); // add point with pen down
        arm.inverseKinematic(420, 120);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(420,180,true); // add point with pen down
        arm.inverseKinematic(420,180);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(360,180,true); // add point with pen down
        arm.inverseKinematic(360,180);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(360,120,true); // add point with pen down
        arm.inverseKinematic(360,120);
        UI.clearGraphics();
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(420,120,true); // add point with pen down
        arm.inverseKinematic(420, 120);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(420,180,true); // add point with pen down
        arm.inverseKinematic(420,180);
        arm.draw();
        drawing.draw();
        drawing.print_path();
    }
    
    public void drawCircle(){
        double r = 75/2;
        int cx = 300; //250
        int cy = 120; //100
        double x = cx + r*Math.cos(0);
        double y = cy + r*Math.sin(0);
        drawing.add_point_to_path(x,y,false); // add point with pen down
        for (double i = 0; i < 2*Math.PI; i=i+0.1){
            UI.clearGraphics();
            x = cx + r*Math.cos(i);
            y = cy + r*Math.sin(i);
            drawing.add_point_to_path(x,y,true); // add point with pen down
            arm.inverseKinematic(x, y);
            arm.draw();
            drawing.draw();
            drawing.print_path();
        }
    }
    
    public void drawSkynet(){
        double r = 50/4;
        int cx = 270; // 230
        int cy = 150; // 200
        double x = cx + r*Math.cos(2*Math.PI);
        double y = cy + r*Math.sin(2*Math.PI);
        drawing.add_point_to_path(x,y,false); // add point with pen down
        for (double i = 2*Math.PI; i > 0.5*Math.PI; i=i-0.1){
            UI.clearGraphics();
            x = cx + r*Math.cos(i);
            y = cy + r*Math.sin(i);
            drawing.add_point_to_path(x,y,true); // add point with pen down
            arm.inverseKinematic(x, y);
            arm.draw();
            drawing.draw();
            drawing.print_path();
        }
        cy = 175;
        for (double i = 3*Math.PI/2; i < 2*Math.PI; i=i+0.1){
            UI.clearGraphics();
            x = cx + r*Math.cos(i);
            y = cy + r*Math.sin(i);
            drawing.add_point_to_path(x,y,true); // add point with pen down
            arm.inverseKinematic(x, y);
            arm.draw();
            drawing.draw();
            drawing.print_path();
        }
        for (double i = 0; i < Math.PI; i=i+0.1){
            UI.clearGraphics();
            x = cx + r*Math.cos(i);
            y = cy + r*Math.sin(i);
            drawing.add_point_to_path(x,y,true); // add point with pen down
            arm.inverseKinematic(x, y);
            arm.draw();
            drawing.draw();
            drawing.print_path();
        }
        
        
        drawing.add_point_to_path(300,140,false); // add point with pen down
        arm.inverseKinematic(300,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(300,180,true); // add point with pen down
        arm.inverseKinematic(300,180);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(300,175,false); // add point with pen down
        arm.inverseKinematic(300,175);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(330,140,true); // add point with pen down
        arm.inverseKinematic(330,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(300,175,false); // add point with pen down
        arm.inverseKinematic(300,175);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(330,190,true); // add point with pen down
        arm.inverseKinematic(330,190);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        //y??
        drawing.add_point_to_path(340,140,false); // add point with pen down
        arm.inverseKinematic(340,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(355,160,true); // add point with pen down
        arm.inverseKinematic(355,160);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(370,140,false); // add point with pen down
        arm.inverseKinematic(370,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(340,190,true); // add point with pen down
        arm.inverseKinematic(340,190);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        //n?
        drawing.add_point_to_path(380,190,false); // add point with pen down
        arm.inverseKinematic(380,190);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(380,140,true); // add point with pen down
        arm.inverseKinematic(380,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(410,190,true); // add point with pen down
        arm.inverseKinematic(410,190);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(410,140,true); // add point with pen down
        arm.inverseKinematic(410,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(450,140,false); // add point with pen down
        arm.inverseKinematic(450,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(420,140,true); // add point with pen down
        arm.inverseKinematic(420,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(420,165,true); // add point with pen down
        arm.inverseKinematic(420,165);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(450,165,false);
        arm.inverseKinematic(450,165);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(420,165,true);
        arm.inverseKinematic(420,165);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(420,190,true);
        arm.inverseKinematic(420,190);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(450,190,true);
        arm.inverseKinematic(450,190);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(460,140,false);
        arm.inverseKinematic(460,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(490,140,true);
        arm.inverseKinematic(490,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(475,140,false);
        arm.inverseKinematic(475,140);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(475,190,true);
        arm.inverseKinematic(475,190);
        arm.draw();
        drawing.draw();
        drawing.print_path();
    }

//    public void drawCircle(){
//        double r = 75/2;
//        int cx = 250;
//        int cy = 150;
//        double x = cx + r*Math.cos(0);
//        double y = cy + r*Math.sin(0);
//        drawing.add_point_to_path(x,y,false); // add point with pen down
//        for (double i = 0; i < 2*Math.PI; i=i+0.1){
//            UI.clearGraphics();
//            x = cx + r*Math.cos(i);
//            y = cy + r*Math.sin(i);
//            drawing.add_point_to_path(x,y,true); // add point with pen down
//            arm.inverseKinematic(x, y);
//            arm.draw();
//            drawing.draw();
//            drawing.print_path();
//        }
//    }

//    public void drawSkynet(){
//        double r = 50/4;
//        int cx = 230;
//        int cy = 200;
//        double x = cx + r*Math.cos(2*Math.PI);
//        double y = cy + r*Math.sin(2*Math.PI);
//        drawing.add_point_to_path(x,y,false); // add point with pen down
//        for (double i = 2*Math.PI; i > 0.5*Math.PI; i=i-0.1){
//            UI.clearGraphics();
//            x = cx + r*Math.cos(i);
//            y = cy + r*Math.sin(i);
//            drawing.add_point_to_path(x,y,true); // add point with pen down
//            arm.inverseKinematic(x, y);
//            arm.draw();
//            drawing.draw();
//            drawing.print_path();
//        }
//        cy = 225;
//        for (double i = 3*Math.PI/2; i < 2*Math.PI; i=i+0.1){
//            UI.clearGraphics();
//            x = cx + r*Math.cos(i);
//            y = cy + r*Math.sin(i);
//            drawing.add_point_to_path(x,y,true); // add point with pen down
//            arm.inverseKinematic(x, y);
//            arm.draw();
//            drawing.draw();
//            drawing.print_path();
//        }
//        for (double i = 0; i < Math.PI; i=i+0.1){
//            UI.clearGraphics();
//            x = cx + r*Math.cos(i);
//            y = cy + r*Math.sin(i);
//            drawing.add_point_to_path(x,y,true); // add point with pen down
//            arm.inverseKinematic(x, y);
//            arm.draw();
//            drawing.draw();
//            drawing.print_path();
//        }
//        
//        
//        drawing.add_point_to_path(260,190,false); // add point with pen down
//        arm.inverseKinematic(260,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(260,240,true); // add point with pen down
//        arm.inverseKinematic(260,240);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(260,225,false); // add point with pen down
//        arm.inverseKinematic(260,225);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(290,190,true); // add point with pen down
//        arm.inverseKinematic(290,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(260,225,false); // add point with pen down
//        arm.inverseKinematic(260,225);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(290,240,true); // add point with pen down
//        arm.inverseKinematic(290,240);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(300,190,false); // add point with pen down
//        arm.inverseKinematic(300,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(315,210,true); // add point with pen down
//        arm.inverseKinematic(300,210);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(330,190,false); // add point with pen down
//        arm.inverseKinematic(300,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(300,240,true); // add point with pen down
//        arm.inverseKinematic(300,240);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(340,240,false); // add point with pen down
//        arm.inverseKinematic(300,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(340,190,true); // add point with pen down
//        arm.inverseKinematic(340,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(370,240,true); // add point with pen down
//        arm.inverseKinematic(370,240);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(370,190,true); // add point with pen down
//        arm.inverseKinematic(370,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(410,190,false); // add point with pen down
//        arm.inverseKinematic(410,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(380,190,true); // add point with pen down
//        arm.inverseKinematic(380,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(380,215,true); // add point with pen down
//        arm.inverseKinematic(380,215);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(410,215,false);
//        arm.inverseKinematic(410,215);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(380,215,true);
//        arm.inverseKinematic(380,215);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(380,240,true);
//        arm.inverseKinematic(380,240);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(410,240,true);
//        arm.inverseKinematic(410,240);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(420,190,false);
//        arm.inverseKinematic(420,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(450,190,true);
//        arm.inverseKinematic(450,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(435,190,false);
//        arm.inverseKinematic(435,190);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//        drawing.add_point_to_path(435,240,true);
//        arm.inverseKinematic(435,240);
//        arm.draw();
//        drawing.draw();
//        drawing.print_path();
//    }

    public void save_xy(){
        state = 0;
        String fname = UIFileChooser.save();
        drawing.save_path(fname);
    }

    public void enter_path_xy(){
        state = 2;
    }

    public void inverse(){
        state = 1;
        arm.draw();
    }

    public void load_xy(){
        state = 0;
        String fname = UIFileChooser.open();
        drawing.load_path(fname);
        drawing.draw();

        arm.draw();
    }

    // save angles into the file
    public void save_ang(){
        String fname = UIFileChooser.open();
        tool_path.convert_drawing_to_angles(drawing,arm,fname);
    }

    public void load_ang(){
    }
    
    // save pwm values into file
    public void save_pwm(){
        state = 0;
        String fname = UIFileChooser.save();
        if(tool_path == null) UI.println("TOOL IS NULL");
        tool_path.convert_angles_to_pwm(drawing, arm, fname);
        tool_path.save_pwm_file(fname);

    }

    public void load_pwm(){

    }

    public void run() {
        while(true) {
            arm.draw();
            UI.sleep(20);
        }
    }

    public static void main(String[] args){
        Main obj = new Main();
    }    

}
