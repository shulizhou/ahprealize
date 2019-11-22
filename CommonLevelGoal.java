package eurekademo.ahp;

import org.apache.commons.lang.StringUtils;

import java.io.FileNotFoundException;
import java.util.List;

import static org.apache.commons.lang.StringUtils.indexOf;

public class CommonLevelGoal extends AhpLeverGoal {
    /**
     * 数据组的长度与宽度用于对应矩阵
     *
     * @param leng
     */
    public CommonLevelGoal(int leng) {
        initLevle(leng);
    }

    /**
     * 根据数据文件的第一行获取数据的标签。
     * @param str
     */
    private void getLableByTitle(String str) {
        String _temp = str.substring(StringUtils.indexOf(str, ',') + 1, str.length());
        this.setLable(_temp.split(","));
    }

    @Override
    public List<String> loadData(String path, String name) throws FileNotFoundException {
        List<String> _tempList = super.loadData(path, name);
        String _str = "";
        /**
         * 获取数据文件的第一行，用于数据的label
         */
        getLableByTitle(_tempList.get(0));
        //这个地方不能用foreach去遍历list，不能过滤第一行标题行
        for (int i = 1; i < _tempList.size(); i++) {
            _str = _tempList.get(i);
           /* System.out.println("str:" + _str);
            System.out.println("getRowData"+getRowData(_str).toString());*/
            //以行为单位对原始数据数组进行赋值
            level[i - 1] = getRowData(_str);
            //  System.out.println("-------");
        }
        return _tempList;
    }

    private Double[] getRowData(final String str) {
        String[] _arr = str.split(",");
        Double[] _row = new Double[_arr.length - 1];
        String[] _arrDouble;
        for (int j = 1; j < _arr.length; j++) {
            //   System.out.print("  _arr[" + j + "]" + _arr[j]);
            if (_arr[j].contains("/")) {
                _arrDouble = _arr[j].split("/");
                _row[j - 1] = Double.valueOf(_arrDouble[0]) / Double.valueOf(_arrDouble[1]);
            } else {
                _row[j - 1] = Double.valueOf(_arr[j]);
            }
        }
        return _row;
    }

    @Override
    public void calLevelRidModeRow() {
        super.calLevelRidModeRow();
    }


    @Override
    public void calSumLevelCol() {
        super.calSumLevelCol();
    }

    @Override
    public void calLwvelModel() {
        super.calLwvelModel();
    }

    @Override
    public void calSumModelRow() {
        super.calSumModelRow();
    }

    @Override
    public void calSumLRMRrow() {
        super.calSumLRMRrow();
    }

    @Override
    public void calModLRMRrow() {
        super.calModLRMRrow();
    }

    public void calMaxValue() {
        Double _temp = 0d;
        for (Double t : this.getModLRMRrow()) {
            _temp = _temp + t;
        }
        this.setMaxValue(_temp / this.getLevel().length);
        System.out.println("---------最大特征根输出-------------");
        System.out.println("最大特征根MaxValue：" + this.getMaxValue());
    }

    public void calCiValue() {
        this.setCiValue((this.getMaxValue() - this.getLevel().length) / (this.getLevel().length - 1));
        System.out.println("-----CI-----:" + this.getCiValue());
        if (this.getCiValue() <= this.getCiStand()) {
            System.out.println("-----CI-----:=" + this.getCiValue() + "在临界值" + this.getCiStand() + "范围内");
        } else {
            System.out.println("-----CI-----:=" + this.getCiValue() + "大于临界值" + this.getCiStand());
        }
    }

    public void calRiValues() {
        this.setRiValue(this.getRiValueByIndex(this.getLevel().length));
        System.out.println("RI" + this.getRiValue());
    }

    public void calCrValue() {
        this.setCrValue(this.getCiValue() / this.getRiValue());
    }

    public void calValue() {
        this.calSumLevelCol();
        this.calLwvelModel();
        this.calSumModelRow();
        this.calLevelRidModeRow();
        this.calSumLRMRrow();
        this.calModLRMRrow();
        this.calMaxValue();
        this.calCiValue();
        this.calRiValues();
        this.calCrValue();
        this.outCalValue();
    }
}
