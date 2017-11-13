# QuantityView
Android quantity view with add and remove button to simply use as a complex widget with handful of quick customizations.

[ ![Download](https://api.bintray.com/packages/jjhesk/maven/QuantityView/images/download.svg) ](https://bintray.com/jjhesk/maven/QuantityView/_latestVersion)

### Sample Screen
![QuantityView](https://raw.githubusercontent.com/himanshu-soni/QuantityView/master/screenshots/device-2015-09-29-191352.png)
![QuantityView](https://raw.githubusercontent.com/himanshu-soni/QuantityView/master/screenshots/device-2015-10-09-175354.png)
![QuantityView](https://raw.githubusercontent.com/himanshu-soni/QuantityView/master/screenshots/device-2015-10-09-175420.png)

- [x] the quantity view
- [x] the label for the quantity view

### Installation
add gradle dependency to your dependency list:

``` groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

	dependencies {
	        compile 'com.hkm.ui:QuantityView:{latest-version}'
	}
```

### Use
1. Include `QuantityView` in your xml.

``` xml
<me.himanshusoni.quantityview.QuantityView
	xmlns:app="http://schemas.android.com/apk/res-auto"
    android:id="@+id/quantityView_default"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:layout_marginTop="10dp"
    app:qv_quantity="10" />
```


### Customization
Attributes:

``` xml
app:qv_addButtonBackground="color|drawable"
app:qv_addButtonText="string"
app:qv_addButtonTextColor="color"
app:qv_removeButtonBackground="color|drawable"
app:qv_removeButtonText="string"
app:qv_removeButtonTextColor="color"
app:qv_quantityBackground="color|drawable"
app:qv_quantityTextColor="color"
app:qv_quantity="integer"
app:qv_quantityPadding="dimension"
app:qv_maxQuantity="integer"
app:qv_quantityButtonsPadding="dimension"
app:qv_addButtonIcon="drawable"
app:qv_removeButtonIcon="drawable"
app:qv_quantityTextSize="24sp"

```


#### Change Log

###### v1.3.1
- fixed issue with OnQuantityChangeListener editing with dialog 
###### v1.3.0
- buttons padding
- buttons icons
-updated support lib to 25.3.1
###### v1.2.0
- old and new quantity in `OnQuantityChangeListener`.

###### v1.1.0
- added option to use custom click listener on quantity for custom view.

==================
developed to make programming easy.

by Himanshu Soni (himanshusoni.me@gmail.com), jjhesk (jobhesk@gmail.com)



### Thank you for your support and we will bring more amazing libraries to your productive works. We are accepting bitcoin by the address as below. Please scan the QR code to start
![wallet](http://s32.postimg.org/sdd1oio1t/qrwallet.jpg)

