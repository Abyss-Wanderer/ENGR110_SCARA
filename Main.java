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
        UI.addButton("Square", this::drawSquare);
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

    public void drawSquare(){        
        drawing.add_point_to_path(270,90,true); // add point with pen down
        arm.inverseKinematic(270, 90);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(300,90,true); // add point with pen down
        arm.inverseKinematic(300, 90);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(300,110,true); // add point with pen down
        arm.inverseKinematic(300,110);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(270,110,true); // add point with pen down
        arm.inverseKinematic(270,110);
        arm.draw();
        drawing.draw();
        drawing.print_path();
        drawing.add_point_to_path(270,90,true); // add point with pen down
        arm.inverseKinematic(270,90);
        arm.draw();
        drawing.draw();
        drawing.print_path();
    }

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
