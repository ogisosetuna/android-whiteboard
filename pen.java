package com.app.superxlcr.mypaintboard.controller;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.app.superxlcr.mypaintboard.model.Line;
import com.app.superxlcr.mypaintboard.model.Point;
import com.app.superxlcr.mypaintboard.model.Protocol;
import com.app.superxlcr.mypaintboard.utils.ProtocolListener;

import org.json.JSONArray;
import org.json.JSONException; 

public class MainActivity extends Activity {
 private ImageView iv;
 private Bitmap baseBitmap;
 private Canvas canvas;
 private Paint paint;
 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  this.iv = (ImageView) this.findViewById(R.id.iv);
  // 创建一张空白图片
  baseBitmap = Bitmap.createBitmap(480, 640, Bitmap.Config.ARGB_8888);
  // 创建一张画布
  canvas = new Canvas(baseBitmap);
  // 画布背景为灰色
  canvas.drawColor(Color.GRAY);
  // 创建画笔
  paint = new Paint();
  // 画笔颜色为红色
  paint.setColor(Color.RED);
  // 宽度5个像素
  paint.setStrokeWidth(5);
  // 先将灰色背景画上
  canvas.drawBitmap(baseBitmap, new Matrix(), paint);
  iv.setImageBitmap(baseBitmap);

  iv.setOnTouchListener(new OnTouchListener() {
   int startX;
   int startY;

   @Override
   public boolean onTouch(View v, MotionEvent event) {
    switch (event.getAction()) {
    case MotionEvent.ACTION_DOWN:
     // 获取手按下时的坐标
     startX = (int) event.getX();
     startY = (int) event.getY();
     break;
    case MotionEvent.ACTION_MOVE:
     // 获取手移动后的坐标
     int stopX = (int) event.getX();
     int stopY = (int) event.getY();
     // 在开始和结束坐标间画一条线
     canvas.drawLine(startX, startY, stopX, stopY, paint);
     // 实时更新开始坐标
     startX = (int) event.getX();
     startY = (int) event.getY();
     iv.setImageBitmap(baseBitmap);
     break;
    }
    return true;
   }
  });
 }

 public void save(View view) {
  try {
   File file = new File(Environment.getExternalStorageDirectory(),
     System.currentTimeMillis() + ".jpg");
   OutputStream stream = new FileOutputStream(file);
   baseBitmap.compress(CompressFormat.JPEG, 100, stream);
   stream.close();
   // 模拟一个广播，通知系统sdcard被挂载
   Intent intent = new Intent();
   intent.setAction(Intent.ACTION_MEDIA_MOUNTED);
   intent.setData(Uri.fromFile(Environment
     .getExternalStorageDirectory()));
   sendBroadcast(intent);

   Toast.makeText(this, "保存图片成功", 0).show();
  } catch (Exception e) {
   Toast.makeText(this, "保存图片失败", 0).show();
   e.printStackTrace();
  }
 }

}
