package eurekademo.ahp;

import sun.net.www.ApplicationLaunchException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AhpLeverGoal {
    /**
     * RI的参考值
     */
    private final Double riArr[] = {0.0, 0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49, 1.51};
    /**
     * CI的参考值
     */
    private final Double ciArr[] = {0.0, 0.0, 0.0, 0.049, 0.092, 0.122, 0.142, 0.161, 0.169, 0.178, 0.185, 0.194};

    private Double riValue = 0d;
    /**
     * 矩阵的一致性比率
     */
    private Double ciValue = 0d;

    /**
     * 矩阵的一致性Cr，cr=ci/ri
     */
    private Double crValue = 0d;

    /**
     * 原数据的二维数组
     */
    Double level[][];

    public String[] getLable() {
        return lable;
    }

    public void setLable(String[] lable) {
        this.lable = lable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    String title;//标准层名称
    String lable[];
    /**
     * 数据所在的路径
     *
     * @param path 数据的所在文件名
     * @param name
     * @return
     */
    public List<String> loadData(final String path, final String name) throws FileNotFoundException {
        List<String> strList = new ArrayList<String>();
        String Dir = path.charAt(path.length() - 1) == '\\' ? path + name : path + "\\" + name;
        try {
            File file = new File(Dir);
            FileReader fReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fReader);
            String strLine = "";
            while (null != (strLine = bufferedReader.readLine())) {
                strList.add(strLine);
            }
            bufferedReader.close();
            fReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strList;
    }

    /**
     * 初始化level的值
     */
    public void initLevle(int leng) {
        this.setLevel(new Double[leng][leng]);
    }

    /**
     * 原数据归一化处理的的一维数据数组
     */
    Double sumLevelCol[];

    /**
     * 计算sumLevelCol的值
     */
    public void calSumLevelCol() {
        /**
         * 用level第一维的长度初始发整个sumLevelCol数组
         */
        sumLevelCol = new Double[level[0].length];
        for (int i = 0; i < sumLevelCol.length; i++) {
            sumLevelCol[i] = 0d;
            for (int j = 0; j < level.length; j++) {
                sumLevelCol[i] = sumLevelCol[i] + level[j][i];
            }
        }
 /*       for (int i = 0; i < sumLevelCol.length; i++) {
            System.out.println("sumLevelCol[" + i + "]:" + sumLevelCol[i]);
        }*/
    }

    /**
     * 归一相除的结果
     */
    Double levelModel[][];

    /**
     * 计算levelmodel的值
     */
    public void calLwvelModel() {
        /**
         * 在原有数组上生成数组.
         */
        levelModel = new Double[level.length][level[0].length];
        System.out.println("------level-----");
        for (int i = 0; i < level.length; i++) {
            for (int j = 0; j < level[0].length; j++) {
                System.out.print(level[i][j] + ",");
            }
            System.out.println(" ");
        }
        System.out.println("------sumLevelCol-----");
        for (int i = 0; i < sumLevelCol.length; i++) {
            System.out.print(sumLevelCol[i] + ",");
        }
        System.out.println("------end-----");
        //以列为单位遍历数组
        for (int i = 0; i < levelModel[0].length; i++) {
            for (int j = 0; j < levelModel.length; j++) {
                levelModel[j][i] = level[j][i] / sumLevelCol[i];
            }
        }
        for (int i = 0; i < levelModel.length; i++) {
            System.out.print("--------levelModel[" + i + ";");
            for (int j = 0; j < levelModel[0].length; j++) {
                System.out.print("levelModel[" + i + "][" + j + "]:" + levelModel[i][j] + "  ");
            }
            System.out.println(" ");
        }
    }

    /**
     * 计算需要的优先级,相除后再每行归一出处理,优先级
     */
    Double sumModelRow[];

    /**
     * 计算sumModelRow
     */
    public void calSumModelRow() {
        sumModelRow = new Double[levelModel.length];
        for (int i = 0; i < levelModel.length; i++) {
            sumModelRow[i] = 0d;
            for (int j = 0; j < levelModel[0].length; j++) {
                sumModelRow[i] = sumModelRow[i] + levelModel[i][j];
            }
            sumModelRow[i] = sumModelRow[i] / sumModelRow.length;
        }

        System.out.println("-----优先级输出-----");
        for (int i=0;i<sumModelRow.length;i++)
         {
            System.out.println(lable[i].trim()+":" + sumModelRow[i] + ";");
        }
    }

    /**
     * sumModelRow乘以以原始值
     */
    Double levelRidModeRow[][];

    /**
     * 计算levelRidModeRow
     */
    public void calLevelRidModeRow() {
        levelRidModeRow = new Double[level.length][level[0].length];
        for (int i = 0; i < level[0].length; i++) {
            for (int j = 0; j < level.length; j++) {
                levelRidModeRow[j][i] = level[j][i] * sumModelRow[i];
            }
        }
        System.out.println("加权矩阵levelRidModeRow");
        for (int i = 0; i < level[0].length; i++) {
            for (int j = 0; j < level.length; j++) {
                System.out.print("i:" + levelRidModeRow[i][j] + ";");
            }
            System.out.println(" ");
        }
    }

    /**
     * levelRidModeRow每行的和，一致性向量
     */
    Double sumLRMRrow[];

    /**
     * 计算calSumLRMRrow
     */
    public void calSumLRMRrow() {
        sumLRMRrow = new Double[levelRidModeRow.length];
        for (int i = 0; i < levelRidModeRow.length; i++) {
            sumLRMRrow[i] = 0d;
            for (int j = 0; j < levelRidModeRow[0].length; j++) {
                sumLRMRrow[i] = sumLRMRrow[i] + levelRidModeRow[i][j];
            }
        }
        System.out.println("----------加权向量sumLRMRrow------------------");
        for (int j = 0; j < sumLRMRrow.length; j++) {
            System.out.println("i:" + sumLRMRrow[j] + ";");
        }
    }

    /**
     * 一致性权重
     */
    Double modLRMRrow[];

    /**
     * 计算modLRMRrow
     */
    public void calModLRMRrow() {
        modLRMRrow = new Double[sumLRMRrow.length];
        System.out.println("-------一致性权重向量----------------");
        for (int i = 0; i < modLRMRrow.length; i++) {
            modLRMRrow[i] = sumLRMRrow[i] / sumModelRow[i];
            System.out.println(modLRMRrow[i]);
        }

    }

    /**
     * 以下是所有属性的get与set方法
     */
    public Double[][] getLevel() {
        return level;
    }

    public void setLevel(Double[][] level) {
        this.level = level;
    }

    /**
     * 矩阵的特征根
     */
    private Double maxValue = 0d;

    public Double[] getRiArr() {
        return riArr;
    }

    public Double[] getCiArr() {
        return ciArr;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    public Double getCrValue() {
        return crValue;
    }

    public void setCrValue(Double crValue) {
        this.crValue = crValue;
    }

    public Double[] getSumLevelCol() {
        return sumLevelCol;
    }

    public void setSumLevelCol(Double[] sumLevelCol) {
        this.sumLevelCol = sumLevelCol;
    }

    public Double[][] getLevelModel() {
        return levelModel;
    }

    public void setLevelModel(Double[][] levelModel) {
        this.levelModel = levelModel;
    }

    public Double[] getSumModelRow() {
        return sumModelRow;
    }

    public void setSumModelRow(Double[] sumModelRow) {
        this.sumModelRow = sumModelRow;
    }

    public Double[][] getLevelRidModeRow() {
        return levelRidModeRow;
    }

    public void setLevelRidModeRow(Double[][] levelRidModeRow) {
        this.levelRidModeRow = levelRidModeRow;
    }

    public Double[] getSumLRMRrow() {
        return sumLRMRrow;
    }

    public void setSumLRMRrow(Double[] sumLRMRrow) {
        this.sumLRMRrow = sumLRMRrow;
    }

    public Double[] getModLRMRrow() {
        return modLRMRrow;
    }

    public void setModLRMRrow(Double[] modLRMRrow) {
        this.modLRMRrow = modLRMRrow;
    }

    public Double getCiValue() {
        return ciValue;
    }

    public void setCiValue(Double ciValue) {
        this.ciValue = ciValue;
    }

    /**
     * 如果超出riArr的长度代表无法计算
     *
     * @param index
     * @return
     */
    public Double getRiValueByIndex(int index) {
        if (index < 0 || index >= this.getRiArr().length)
            return -1d;
        else
            return this.getRiArr()[index - 1];
    }

    public Double getRiValue() {
        return riValue;
    }

    public void setRiValue(Double riValue) {
        this.riValue = riValue;
    }

    public Double getCiStand() {
        return this.getCiArr()[this.getLevel().length];
    }

    /**
     * 输出最终的矩阵取终结果
     */
    public void outCalValue() {
        System.out.println("      指标个数=" + this.getLevel().length);
        System.out.println("  最大特征根值=" + this.getMaxValue());
        System.out.println("  指标计算CI值=" + this.getCiValue());
        System.out.println("一致性指标RI值=" + this.getRiValue());
        System.out.println("一致性比率CR值=" + this.getCrValue());
        if (this.getCiValue() < this.getCiStand() && this.getCrValue() < 0.1) {
            System.out.println("CI<"+this.getCiStand()+",CR<0.1,计算结果可被接受");
        } else {
            System.out.println("原数据计算结果可能存在偏差");
        }
    }
}
