# TissueAnalyticsAssignment
This Android App displays the stream flow related information fetched from the USGS NWIS site.

Developed app on the following environment
Android Studio version: 2.3.3
Java version: 8
Designed on 5-inch Nexus phone
Works on all the phones of any screen size as scrolling is enabled.

SDK version
•	Minimum SDK Version: 16
•	Target SDK Version: 25
•	Compile SDK Version: 25

To draw Line graphs in the project:
•	Added GraphView-4.2.1.jar to the project library to plot the line graph for the streamflow data fetched from USGS site.
•	Line Graph is drawn on below Assumptions:
X-axis: 
Scale x is plotted based on timing of the streamflow data fetched.
1 point on the x-axis= 15 minutes

Y-axis:
Scale y is plotted based on the streamflow data value.
1 point on y-axis= each data value(ft3/s) fetched.


