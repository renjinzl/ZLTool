package com.renjinzl.zltool.view

import android.app.DownloadManager
import android.content.*
import android.net.Uri
import android.os.Environment
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * name
 * author   renjinzl

 * date     2022-07-30 12:36
 *
 * version  1.0.0   update 2022-07-30 12:36   起航
 * @since 描述
 */

class DownloadAction{
    /**
     * 进度
     */
    var progress = 0

    var id : Long = 0

    var manager : DownloadManager? = null
}

private fun String.getDownName() : String{
    var name = this
    if (name.contains("?")){
        name = name.substring(0, lastIndexOf("?"))
    }

    if (name.contains("/")){
        name = name.substring(name.lastIndexOf("/") + 1, name.length)
    }

    return System.currentTimeMillis().toString()+name
}
/**
 * 下载
 */
fun String.down(context: Context){
    if (this.isNotEmpty()){
        val name = getDownName()
        down(context,this,name).apply {
            (context as AppCompatActivity).lifecycleScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                manager?.let {
                    var progress = 0
                    while (progress <= 100) {
                        progress = getDownloadProgress(it,id)
                        if (progress == 100){
                            //设置图片保存的路径和文件名
                            val downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()
                            val imagePath = "$downloadPath/$name"
//                            try {
//                                MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, name, "123")
//                            } catch (e: FileNotFoundException) {
//                                e.printStackTrace()
//                            }catch (e: IllegalArgumentException) {
//                                e.printStackTrace()
//                            }
                            context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://$downloadPath")))
                            break
                        } else {
                            delay(200)
                        }
                    }
                }
            }
        }
    }
}

/**
 * 下载
 */
fun String.down(context: Context,block : (DownloadAction) -> Unit){
    if (this.isNotEmpty()){
        val name = getDownName()
        val action = down(context,this,name)
        action.manager?.let { manager ->
            (context as AppCompatActivity).lifecycleScope.launch(Dispatchers.Main, CoroutineStart.DEFAULT) {
                var progress = 0
                while (progress in 0..100) {
                    progress = getDownloadProgress(manager,action.id)
                    action.progress = progress
                    Log.d("--String.down--", "progress:$progress")
                    block(action)

                    if (progress == 100){
                        progress = -1
                    }
                    delay(200)

                }
            }
        }

    }
}

private fun down(context: Context, url: String, name: String): DownloadAction {
    val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    val request = DownloadManager.Request(Uri.parse(url)) //添加下载文件的网络路径
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name) //添加保存文件路径与名称
    request.setTitle(name) //添加在通知栏里显示的标题
    request.setDescription("下载中") //添加在通知栏里显示的描述
//    request.addRequestHeader("token", "11") //如果你的下载需要token，或者有秘钥要求，可以在此处添加header的 key： value
    if (url.endsWith("apk")){
        request.setMimeType("application/vnd.android.package-archive")
    }
    request.allowScanningByMediaScanner()
    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE or DownloadManager.Request.NETWORK_WIFI) //设置下载的网络类型
    request.setVisibleInDownloadsUi(false) //是否显示下载 从Android Q开始会被忽略
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //下载中与下载完成后都会在通知中显示| 另外可以选 DownloadManager.Request.VISIBILITY_VISIBLE 仅在下载中时显示在通知中,完成后会自动隐藏

    val downloadId = downloadManager.enqueue(request)//加入队列，会返回一个唯一下载id

    return DownloadAction().apply {
        id = downloadId
        manager = downloadManager
    }
}

class DownloadReceiver : BroadcastReceiver(){
    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.apply {
            val action = intent.action
            if (!TextUtils.equals(action, DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                return
            }
            val downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (downloadId == -1L) {
                return
            }
            val downloadManager = context!!.applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val status: Int = getDownloadStatus(downloadManager, downloadId)
            if (status != DownloadManager.STATUS_SUCCESSFUL) { //下载状态不等于成功就跳出
                return
            }
            val uri = downloadManager.getUriForDownloadedFile(downloadId) ?: return //获取下载完成文件uri
        }
    }

    /**
     * 获取下载状态
     * @param downloadManager
     * @param downloadId
     * @return
     */
    private fun getDownloadStatus(downloadManager: DownloadManager, downloadId: Long): Int {
        val query = DownloadManager.Query().setFilterById(downloadId)
        downloadManager.query(query)?.use { c ->
            if (c.moveToFirst()) {
                return c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS))
            }
        }
        return -1
    }

}

/**
 * 获取当前下载进度
 *
 * @return
 */
fun getDownloadProgress(downloadManager: DownloadManager, downloadId: Long): Int {
    val query = DownloadManager.Query().setFilterById(downloadId)
    downloadManager.query(query)?.use { c ->
        if (c.moveToFirst()) {
            /**
             * 总大小
             */
            val totalSizeBytesIndex = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

            /**
             * 下载了 73814292：totalSizeBytesIndex73814292
             *       73814292
             */
            val bytesDownloadSoFarIndex = c.getInt(c.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))

            Log.d("--String.down--", "bytesDownloadSoFarIndex:$bytesDownloadSoFarIndex：totalSizeBytesIndex$totalSizeBytesIndex")
            val scale = bytesDownloadSoFarIndex.divide(totalSizeBytesIndex,2).toDouble()
            val progress = scale.multiply(100.0).toInt()
            c.close()
            return progress
        }
    }
    return -1
}

/**
 * 复制到剪切板
 */
fun String.copy(context: Context) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // 创建普通字符型ClipData(第一个参数随便传)
    val mClipData = ClipData.newPlainText("text", this)
    // 将ClipData内容放到系统剪贴板里。
    clipboard.setPrimaryClip(mClipData)
}

fun String.log() {
    Log.d("zld", this)
}
fun String.log(tag : String) {
    Log.d(tag, this)
}
