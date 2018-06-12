# YinLayout
一个自定义布局库

# 引入
compile 'com.yinzihao:YinLayout:{latest-version}'

# 使用指南

## CommonCheckableGroup
多选或单选项的父布局，类似于`{@link android.widget.RadioGroup}`。
直接子 `view` 需要实现`{@link Checkable}`接口或利用框架中的`{@link CheckableTag}`(事实上是一个实现了`{@link Checkable}`接口的`{@link FrameLayout}`)包裹才能被监听选中状态。

- 在 xml 中使用
```
<com.source.yin.yinlayout.checkable.CommonCheckableGroup
    android:id="@+id/common_checkable_group"
    android:layout_height="wrap_content"
    android:layout_width="match_parent"
    app:multiple="true"
    android:orientation="vertical">

    <CheckedTextView
        android:background="@drawable/background_selector"
        android:layout_height="wrap_content"
        android:layout_width="match_parent"
        android:padding="10dp"
        android:text="选项1"
        android:textColor="@android:color/white" />

    <com.source.yin.yinlayout.checkable.CheckableTag
        android:background="@drawable/background_selector"
        android:layout_height="wrap_content"
        android:layout_marginTop="5dp"
        android:layout_width="match_parent">

        <TextView
            android:layout_height="wrap_content"
            android:layout_width="wrap_content"
            android:padding="10dp"
            android:text="选项2"
            android:textColor="@android:color/white" />

    </com.source.yin.yinlayout.checkable.CheckableTag>
</com.source.yin.yinlayout.checkable.CommonCheckableGroup>
```
将选项作为 `CommonCheckableGroup` 的子 `view` 即可实现所需效果。
xml 中设置 `multiple` 属性为 `ture` 则表示可多选。

- 在代码中使用

```
        commonCheckableGroup.setLayoutAdapter(new BaseLayoutAdapter<String>(getApplicationContext(), stringList, R.layout.checkable_group_layout_item) {
            @Override
            public void dataBind(View itemView, int position, String data) {
                TextView textView = (TextView) itemView.findViewById(R.id.tv_item);
                textView.setText(data);
            }
        });
```
在代码中通过 adapter 的方式可以更灵活的设置多个选项，`BaseLayoutAdapter` 构造函数中的第3个参数中的 xml 即每个选项的布局文件，会在 `dataBind()` 回调中解析为 `view` 返回。使用者在此方法中将数据源与布局文件绑定显示。

```
        commonCheckableGroup.setCheckedListener(new CommonCheckableGroup.CheckedListener() {
            @Override
            public void onCheckChange(Checkable checkable) {
                List<Checkable> checkedItemList = commonCheckableGroup.getCheckedItemList();
            }
        });
```
通过 `setCheckedListener()` 设置选中事件监听。通过 `getCheckedItemList()` 得到当前选中的对象列表。

<div align=center><img width="270" height="510" src="https://github.com/YinZiHao1994/YinLayout/blob/master/demoimage/common_check_layout.gif"/></div>

## FlowLayout
流式布局。子 `view` 将横向依次填满布局的每一行。

`FlowLayout` 与 `CommonCheckableGroup` 类似，都可在 xml 中直接使用或在代码中通过 adapter 方式动态使用。事实上，它们都实现了 [LayoutByAdapterAble](#layoutbyadapterable) 接口，所以符合同样的 adapter 实现规范。

## CheckableGroupFlowLayout
用于选择标签的流式布局。继承了 `FlowLayout`，所以显示样式与 `FlowLayout` 相同。同时与 `CommonCheckableGroup` 一样实现了 [CheckableGroup](#checkablegroup) 接口，所以同样可通过 `setOnItemCheckListener()` 设置选项状态改变监听和通过 `getCheckedItemList()` 获取当前选中项的列表。

<div align=center><img width="270" height="510" src="https://github.com/YinZiHao1994/YinLayout/blob/master/demoimage/flow_layout.gif"/></div>


## FlipView
轮播控件，类似`{@link android.support.v4.view.ViewPager}`，但实现更简单、并且可控制动画效果、动画时长。适用于简单的文字、图片等的轮播。

<div align=center><img width="270" height="510" src="https://github.com/YinZiHao1994/YinLayout/blob/master/demoimage/flip_view.gif"/></div>

## SideMenuLayout
横划菜单 `ViewGroup`,常见于列表

```
<com.source.yin.yinlayout.sidemenulayout.SideMenuLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="1dp">

    <TextView
        android:id="@+id/tv_content"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="content"
        android:textColor="@android:color/white"
        android:textSize="40sp" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@android:color/holo_blue_light"
        android:text="menu"
        android:textColor="@android:color/white"
        android:textSize="40sp" />

    <TextView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:background="@android:color/holo_red_light"
        android:text="other_menu"
        android:textColor="@android:color/white"
        android:textSize="40sp" />

</com.source.yin.yinlayout.sidemenulayout.SideMenuLayout>
```

`SideMenuLayout` 中的第一个子 `view` 将作为内容，后面的子 `view` 将作为菜单。
通过 `setOnMenuClickListener()` 方法设置菜单项的点击事件监听。

<div align=center><img width="270" height="510" src="https://github.com/YinZiHao1994/YinLayout/blob/master/demoimage/side_menu.gif"/></div>

## <span id="CheckableGroup">CheckableGroup</span>
子 view 可被选中的 viewGroup 的统一接口

## <span id="LayoutByAdapterAble">LayoutByAdapterAble</span>
可使用 adapter 方式实现数据管理的布局统一实现的接口
