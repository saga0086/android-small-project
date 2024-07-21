package com.example.showwmtproduct.util

import android.os.Bundle
import com.example.showwmtproduct.data.Constants.BUNDLE_NON_STRING
import com.example.showwmtproduct.data.Constants.BUNDLE_PRODUCT_KEY_DESCRIPTION
import com.example.showwmtproduct.data.Constants.BUNDLE_PRODUCT_KEY_ID
import com.example.showwmtproduct.data.Constants.BUNDLE_PRODUCT_KEY_TITLE
import com.example.showwmtproduct.data.Constants.BUNDLE_PRODUCT_KEY_URL
import com.example.showwmtproduct.data.Product

class ProductUtil {
    companion object{
        fun productToBundle(product: Product): Bundle {
            val bundle = Bundle()
            bundle.putInt(BUNDLE_PRODUCT_KEY_ID, product.id)
            bundle.putString(BUNDLE_PRODUCT_KEY_URL, product.url)
            bundle.putString(BUNDLE_PRODUCT_KEY_TITLE, product.title)
            bundle.putString(BUNDLE_PRODUCT_KEY_DESCRIPTION, product.description)
            return bundle
        }

        fun bundleToProduct(bundle: Bundle): Product {
            return Product(bundle.getString(BUNDLE_PRODUCT_KEY_URL) ?: BUNDLE_NON_STRING,
                bundle.getString(BUNDLE_PRODUCT_KEY_TITLE) ?: BUNDLE_NON_STRING,
                bundle.getInt(BUNDLE_PRODUCT_KEY_ID),
                bundle.getString(BUNDLE_PRODUCT_KEY_DESCRIPTION) ?: BUNDLE_NON_STRING)
        }
    }
}