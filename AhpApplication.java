package eurekademo.ahp;

import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AhpApplication {
    private static CommonLevelGoal goal;
    private static CommonLevelGoal b1;
    private static CommonLevelGoal b2;
    private static CommonLevelGoal b3;
    private static CommonLevelGoal b4;
    private static List<CommonLevelGoal> listlayer;
    private static MergeValues calGoal;
    public static void main(String[] args) throws Exception {

        /**
         * 数据文件所在的位置
         */
        String path = "";
        /**
         * 每一个目标文件的名称
         */
        String _temp = "";

        /**控制台控制读取数据
         *
         */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("----------请输入标准层宽度-------------------");
        //读取数据文件名称
        int intial = Integer.parseInt(br.readLine());
        System.out.println("----------请输入目标层宽度-------------------");
        int layer = Integer.parseInt(br.readLine());
        System.out.println("----------请输入数据所在路径-------------------");
        path = br.readLine();//读取数据文件名称
        /**
         * 目标层计算
         */
        goal = new CommonLevelGoal(intial);
        System.out.println("----------请输入第一层数据文件名称-------------");
        _temp = br.readLine();
        _temp = StringUtils.isBlank(_temp) ? "goal" : _temp;//读取数据文件名称
        goal.loadData(path, _temp);
        goal.calValue();

        listlayer = new ArrayList<>();
        for(int i=0; i<intial; i++){
            CommonLevelGoal _tempLayer = new CommonLevelGoal(layer);
            _tempLayer.setTitle("第"+(i+1)+"个标准层");
            System.out.println("----------请输入第"+(i+1)+"层次单数据文件名称---------");
            _temp = br.readLine();//读取数据文件名称
            _tempLayer.loadData(path, _temp);
            _tempLayer.calValue();
            listlayer.add(_tempLayer);
        }
        calGoal = new MergeValues(goal,listlayer);
        calGoal.calValues();


    }
}
