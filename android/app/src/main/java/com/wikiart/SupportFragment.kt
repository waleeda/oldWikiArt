package com.wikiart

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.compose.ui.platform.ComposeView
import com.android.billingclient.api.AcknowledgePurchaseParams
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.ProductDetails
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.PurchasesUpdatedListener
import com.android.billingclient.api.QueryProductDetailsParams

class SupportFragment : Fragment(), PurchasesUpdatedListener {
    private lateinit var billingClient: BillingClient
    private val productIds = listOf(
        "wikiart.support4",
        "wiki.level3",
        "wikiart.level2",
        "wikiart.support1",
        "generous"
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return ComposeView(requireContext()).apply {
            setContent {
                SupportScreen(
                    onSendFeedback = {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = Uri.parse("mailto:wikiartfeedback@icloud.com")
                        }
                        startActivity(intent)
                    },
                    onDonate = { queryProductsAndShowDialog() }
                )
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        billingClient = BillingClient.newBuilder(requireContext())
            .enablePendingPurchases()
            .setListener(this)
            .build()
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(result: BillingResult) {}
            override fun onBillingServiceDisconnected() {}
        })

        // No view IDs to initialize when using Compose UI
    }

    override fun onDestroyView() {
        super.onDestroyView()
        billingClient.endConnection()
    }

    private fun queryProductsAndShowDialog() {
        val products = productIds.map {
            QueryProductDetailsParams.Product.newBuilder()
                .setProductId(it)
                .setProductType(BillingClient.ProductType.INAPP)
                .build()
        }

        val params = QueryProductDetailsParams.newBuilder()
            .setProductList(products)
            .build()

        billingClient.queryProductDetailsAsync(params) { result, list ->
            if (result.responseCode == BillingClient.BillingResponseCode.OK && list.isNotEmpty()) {
                showProductDialog(list)
            } else {
                Toast.makeText(requireContext(), R.string.unable_load_products, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showProductDialog(details: List<ProductDetails>) {
        val sorted = details.sortedByDescending {
            it.oneTimePurchaseOfferDetails?.priceAmountMicros ?: 0
        }
        val names = sorted.map {
            "${it.title} ${it.oneTimePurchaseOfferDetails?.formattedPrice ?: ""}"
        }
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.support))
            .setItems(names.toTypedArray()) { _, which ->
                launchPurchase(sorted[which])
            }
            .setNegativeButton(android.R.string.cancel, null)
        builder.show()
    }

    private fun launchPurchase(details: ProductDetails) {
        val flowParams = BillingFlowParams.newBuilder()
            .setProductDetailsParamsList(
                listOf(
                    BillingFlowParams.ProductDetailsParams.newBuilder()
                        .setProductDetails(details)
                        .build()
                )
            )
            .build()
        billingClient.launchBillingFlow(requireActivity(), flowParams)
    }

    override fun onPurchasesUpdated(result: BillingResult, purchases: MutableList<Purchase>?) {
        if (result.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (purchase in purchases) {
                handlePurchase(purchase)
            }
        }
    }

    private fun handlePurchase(purchase: Purchase) {
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED && !purchase.isAcknowledged) {
            val params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.purchaseToken)
                .build()
            billingClient.acknowledgePurchase(params) {}
            Toast.makeText(requireContext(), R.string.thank_you, Toast.LENGTH_LONG).show()
        }
    }
}
