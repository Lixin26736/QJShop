import { webview } from '@kit.ArkWeb'

@Entry
@Component
struct Index {

  build() {
    Column() {
      Web({ src: 'http://47.100.214.45:8080/', controller: new webview.WebviewController() })
        .width('100%')
        .height('100%')
        .domStorageAccess(true)
        .javaScriptAccess(true)
        .mixedMode(MixedMode.All)
    }
    .width('100%')
    .height('100%')
  }
}
