
#import <Cordova/CDVPlugin.h>
#import <CoreLocation/CoreLocation.h>

@interface NativeUtils: CDVPlugin
- (void)checkGPSState:(CDVInvokedUrlCommand*)command;
@end