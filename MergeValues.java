package eurekademo.ahp;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

/**
 * 用于对最终计算绪的合并输入出最终的权重。
 */
public class MergeValues {
    private Double[][] finalValue;
    private Double[] endValue;
    /**
     * 目示层指标；
     */
    private AhpLeverGoal goal;

    public List<CommonLevelGoal> getListlevel() {
        return listlevel;
    }

    public void setListlevel(List<CommonLevelGoal> listlevel) {
        this.listlevel = listlevel;
    }

    /**
     * 指示层指标
     */
    private List<CommonLevelGoal> listlevel;

    public MergeValues(CommonLevelGoal goal, List<CommonLevelGoal> layer) {
        this.goal = goal;
        this.listlevel = layer;
    }

    public AhpLeverGoal getGoal() {
        return goal;
    }

    public void setGoal(AhpLeverGoal goal) {
        this.goal = goal;
    }


    public void calValues() {
        //输入出目标计算矩阵
        System.out.println("------输出合并矩阵------");
        System.out.print("  ");
        for (int i = 0; i < goal.getLable().length; i++) {
            System.out.print("-------" + goal.getLable()[i].trim() + "");
        }
        System.out.println("  ");
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(3);
        for (int i = 0; i < goal.getLable().length; i++) {
            System.out.print("     " + nf.format(goal.getSumModelRow()[i]));
        }
        System.out.println("");
        for (int i = 0; i < getListlevel().get(0).getSumModelRow().length; i++) {
            System.out.print(getListlevel().get(0).getLable()[i] + ":");
            for (int j = 0; j < getListlevel().size(); j++) {
                System.out.print("     " + nf.format(getListlevel().get(j).getSumModelRow()[i]));
            }
            System.out.println("");
        }

        finalValue = new Double[getListlevel().get(0).getSumModelRow().length][goal.getSumModelRow().length];
        endValue = new Double[getListlevel().get(0).getSumModelRow().length];

        for (int i = 0; i < goal.getSumModelRow().length; i++) {
            for (int j = 0; j < listlevel.size(); j++) {
                for (int k = 0; k < listlevel.get(j).getSumModelRow().length; k++) {
                    finalValue[k][i] = listlevel.get(i).getSumModelRow()[k] * goal.getSumModelRow()[i];
                }
            }

        }
        System.out.println("--------输出组合矩阵--------");
        for (int i = 0; i < finalValue.length; i++) {
            for (int j = 0; j < finalValue[0].length; j++) {
                System.out.print("  " + nf.format(finalValue[i][j]));
            }
            System.out.println(" ");
        }
        System.out.println("--------输出最终组合权重--------");
        for (int i = 0; i < finalValue.length; i++) {
            endValue[i] = 0d;
            for (int j = 0; j < finalValue[0].length; j++) {
                endValue[i] = endValue[i] + finalValue[i][j];
            }
            endValue[i] = endValue[i] / finalValue[0].length;
            System.out.println(listlevel.get(0).getLable()[i] + ":" + nf.format(endValue[i]));
        }
        System.out.println("--------程序运计算结束--------");
    }
}
