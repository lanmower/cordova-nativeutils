//
//  NativeUtils.m
//
//
//  Created by Paul Michael Wisdom on 5/20/15.
//
//

#import "NativeUtils.h"


@implementation NativeUtils

BOOL showPrompt;

- (void)pluginInitialize {
    showPrompt = NO;
}

- (void)getGPSState:(CDVInvokedUrlCommand*)command
{
    CDVPluginResult* pluginResult = nil;
    
    //get command options
    //    NSLog(@"GETTING GPS STATE");
    BOOL locationEnabled = [CLLocationManager locationServicesEnabled];
    //    NSLog(@"Location services enabled? %d", locationEnabled);
    CLAuthorizationStatus authStatus = [CLLocationManager authorizationStatus];
    //    NSLog(@"Location Manager Auth Status %d", authStatus);
    //    NSLog(@"Denied %d Authorized %d AlwaysAuthorized %d WhenInUseAuthorized %d Restricted %d", kCLAuthorizationStatusDenied, kCLAuthorizationStatusAuthorized, kCLAuthorizationStatusAuthorizedAlways, kCLAuthorizationStatusAuthorizedWhenInUse, kCLAuthorizationStatusNotDetermined);
    
    showPrompt = [[command argumentAtIndex:0] boolValue];
    
    if([CLLocationManager locationServicesEnabled] &&
       ((authStatus == kCLAuthorizationStatusAuthorized) ||
        (authStatus == kCLAuthorizationStatusAuthorizedAlways)  ||
        (authStatus == kCLAuthorizationStatusAuthorizedWhenInUse))) {
           
           pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: @"Enabled"];
           
       } else {
           NSString *reason;
           
           if(authStatus == kCLAuthorizationStatusDenied) {
               reason = @"Denied";
           } else if(authStatus == kCLAuthorizationStatusNotDetermined) {
               reason = @"NotDetermined";
           } else if(authStatus == kCLAuthorizationStatusRestricted) {
               reason = @"Restricted";
           } else {
               reason = @"Disabled";
           }
           
           if(showPrompt) {
               UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"GPS Disabled"
                                                               message:@"Please Enable Your GPS To Continue."
                                                              delegate:self
                                                     cancelButtonTitle:@"Go To Settings"
                                                     otherButtonTitles:nil];
               
               [alert show];
           }
           
           pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString: reason];
       }
    
    [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}

- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex {
    [alertView dismissWithClickedButtonIndex:buttonIndex animated:YES];
    [[UIApplication sharedApplication] openURL: [NSURL URLWithString: UIApplicationOpenSettingsURLString]];
}

@end