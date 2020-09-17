package com.eju.cy.videoeditor

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.eju.cy.videoeditor.utils.GifSizeFilter
import com.eju.cy.videoeditor.utils.Glide4Engine
import com.tbruyelle.rxpermissions2.RxPermissions
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.filter.Filter
import com.zhihu.matisse.internal.entity.CaptureStrategy
import io.microshow.rxffmpeg.RxFFmpegCommandList
import io.microshow.rxffmpeg.RxFFmpegInvoke
import io.microshow.rxffmpeg.RxFFmpegSubscriber
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException


class MainActivity : AppCompatActivity(), View.OnClickListener {


    //权限
    private var rxPermissions: RxPermissions? = null

    //需要申请的权限，必须先在AndroidManifest.xml有声明，才可以动态获取权限
    private val PERMISSIONS_STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.CAMERA
    )

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        rxPermissions = RxPermissions(this)
        rxPermissions!!.request(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CAMERA
        )
            .subscribe {
            }


        tv_select_img.setOnClickListener(this)
        tv_show_img.setOnClickListener(this)
        tv_create_video.setOnClickListener(this)


    }

    override fun onClick(v: View?) {


        when (v) {

            tv_select_img -> {

                selectImg()


            }

            tv_create_video -> {
                imgToVideoStyleTranslation(
                    tv_show_img.text.toString(),

                    tv_show_img.text.toString().replace(".jpg", ".mp4")


//                    tv_show_img.text.toString(),
//                    Environment.DIRECTORY_DCIM + "/EJU/"+ System.currentTimeMillis()
//                        .toString() + ".mp4"
                )

            }


        }

    }


    fun imgToVideoStyleTranslation(filePaht: String, savePathName: String) {

//        从左到右
//
//        ffmpeg -y -i 1.jpg -vf "zoompan='1.5':x='if(lte(on,-1),(iw-iw/zoom)/2,x+3)':y='if(lte(on,1),(ih-ih/zoom)/2,y)':d=150"  1.mp4


//        从右到左
//
//        ffmpeg -y -i 1.jpg -vf "zoompan='1.5':x='if(lte(on,1),(iw/zoom)/2,x-3)':y='if(lte(on,1),(ih-ih/zoom)/2,y)':d=150"  1.mp4


//        从上到下
//
//        ffmpeg -y -i 1.jpg -vf "zoompan='1.5':x='if(lte(on,1),(iw-iw/zoom)/2,x)':y='if(lte(on,-1),(ih-ih/zoom)/2,y+2)':d=150"  1.mp4


//        从下到上
//
//        ffmpeg -y -i 1.jpg -vf "zoompan='1.5':x='if(lte(on,1),(iw-iw/zoom)/2,x)':y='if(lte(on,1),(ih/zoom)/2,y-2)':d=150"  1.mp4


        // "ffmpeg -y -i /storage/emulated/0/1/input.mp4 -vf boxblur=5:1 -preset superfast /storage/emulated/0/1/result.mp4"
        var cmd =
            "ffmpeg -y -i /storage/emulated/0/DCIM/tb_image_share_1599921393238.jpg -vf zoompan='1.5':x='if(lte(on,-1),(iw-iw/zoom)/2,x+3)':y='if(lte(on,1),(ih-ih/zoom)/2,y)':d=150 /storage/emulated/0/DCIM/111111111111.mp4"
        val commandsList = cmd.split(" ")


        val cmdlist = RxFFmpegCommandList()
        cmdlist.append("-i")
        cmdlist.append(filePaht)

        //  cmdlist.append("zoompan='1.5':x='if(lte(on,-1),(iw-iw/zoom)/2,x+3)':y='if(lte(on,1),(ih-ih/zoom)/2,y)':d=150")
        cmdlist.append("-vf")
        //cmdlist.append("zoompan='min(zoom+0.01,1.5)':x='if(lte(on,50),(iw-iw/zoom)/2,x+3)':y='if(lte(on,50),(ih-ih/zoom)/2,y)':s=1280x720")//放大，从左到右

        cmdlist.append("zoompan=z='min(zoom+0.0015,1.5)':d=700:x='iw/2-(iw/zoom/2)':y='ih/2-(ih/zoom/2)'")//推近到1.5 并且同时以中心摇的效果:
        //cmdlist.append("zoompan=z='min(zoom+0.0015,1.5)':d=700:x='if(gte(zoom,1.5),x,x+1/a)':y='if(gte(zoom,1.5),y,y+1)':s=640x360")//推近到1.5 并且同时在中心附近摇的效果:
        cmdlist.append(savePathName)
        val commandsArr = cmdlist.build()


        //  开始执行FFmpeg命令
        RxFFmpegInvoke.getInstance()
            .runCommandRxJava(commandsArr)
            .subscribe(object : RxFFmpegSubscriber() {
                override fun onFinish() {
                    LogUtils.w("处理成功")


                    val path = savePathName

                    val file = File(path)
                    if (null != file) {
                        LogUtils.w("文件不为空")
                        ToastUtils.showLong("装换完成")
                        // refreshAlbum(this@MainActivity, file)
                        refreshAlbum(this@MainActivity, path)
                    } else {
                        LogUtils.w("文件为空")
                    }
                    //RxFFmpegInvoke.getInstance().getMediaInfo("/storage/emulated/0/DCIM/cs.mp4")
                }

                override fun onCancel() {
                    LogUtils.w("处理取消")
                }

                override fun onProgress(progress: Int, progressTime: Long) {
                    LogUtils.w("进度" + progress + "\n耗时" + progressTime)
                }

                override fun onError(message: String?) {
                    LogUtils.w("处理失败")
                }
            })


    }

    /**
     * 选择图片
     */
    fun selectImg() {
        Matisse.from(this)
            .choose(MimeType.ofAll(), false)
            .countable(true)// //有序选择图片 123456...
            .capture(true) //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
            .captureStrategy(
                CaptureStrategy(true, "com.eju.cy.videoeditor.fileProvider")
            )
            .maxSelectable(1)//选择1张
            .addFilter(GifSizeFilter(160, 160, 5 * Filter.K * Filter.K))
//            .gridExpectedSize(
//                resources.getDimensionPixelSize(R.dimen.grid_expected_size)
//            )
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
            .thumbnailScale(0.85f) //界面中缩略图的质量
            //
            .imageEngine(Glide4Engine())      //Glide加载方式
            .setOnSelectedListener { uriList, pathList ->
                // DO SOMETHING IMMEDIATELY HERE

            }
            .originalEnable(true)
            .maxOriginalSize(10)
            .setOnCheckedListener {
                // DO SOMETHING IMMEDIATELY HERE
                // Timber.w(("isCheckedonCheck: isChecked=$isChecked")
            }
            .theme(R.style.Matisse_Dracula) //黑色主题
            .forResult(AppTags.REQUEST_CODE_CHOOSE)
    }

    /**
     * 获取选择图片路径
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppTags.REQUEST_CODE_CHOOSE && resultCode == Activity.RESULT_OK) {
            if (Matisse.obtainPathResult(data) != null && Matisse.obtainPathResult(data).size > 0) {
                var picUrl = Matisse.obtainPathResult(data)[0]
                LogUtils.w("图片路径-" + picUrl)

                tv_show_img.text = picUrl
            }


        }
    }

    /**
     * 刷新图库
     */
    fun refreshAlbum(context: Context, file: File) {
        try {
            MediaStore.Images.Media.insertImage(
                context.contentResolver,
                file.absolutePath,
                file.name,
                null
            )
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
    }

    /**
     * 刷新图库
     */
    fun refreshAlbum(context: Context, filePath: String) {

        val file = File(filePath)
        MediaScannerConnection.scanFile(
            context, arrayOf(file.toString()),
            arrayOf(file.getName()), null
        )
    }


}