package com.socialbrothers.android.imageRecognitionSB.Controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.socialbrothers.android.imageRecognitionSB.Otherthings.ProductList;
import com.socialbrothers.android.imageRecognitionSB.R;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
	
	private List<ProductList> productList;
	
	public ProductAdapter(List<ProductList> productList) {
		this.productList = productList;
	}
	
	public void swapList(List<ProductList> productList) {
		this.productList = productList;
		if (productList != null) {
			this.notifyDataSetChanged();
		}
	}
	
	@NonNull
	@Override
	public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
		LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
		View view = inflater.inflate(R.layout.basket_product, viewGroup, false);
		
		return new ViewHolder(view);
	}
	
	@Override
	public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder viewHolder, int i) {
		final ProductList product = productList.get(i);
		DecimalFormat df = new DecimalFormat("€0.00");
		
	//	viewHolder.productImage.setImageResource(product.getResourceId());
		viewHolder.productName.setText(product.getName());
		viewHolder.currentPrice.setText(df.format(product.getCurrentPrice()));
		viewHolder.amountOfProducts.setText(String.valueOf(product.getProductCount()));
		viewHolder.totalPrice.setText(df.format(product.getTotalPrice()));
		
		viewHolder.add.setImageResource(R.drawable.baseline_add_circle_black_24dp);
		viewHolder.add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				df.setRoundingMode(RoundingMode.CEILING);
				product.setProductCount(product.getProductCount() + 1);
				viewHolder.amountOfProducts.setText(String.valueOf(product.getProductCount()));
				viewHolder.totalPrice.setText(df.format(product.getTotalPrice()));
			}
		});
		
		viewHolder.remove.setImageResource(R.drawable.baseline_remove_circle_black_24dp);
		viewHolder.remove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				df.setRoundingMode(RoundingMode.CEILING);
				product.setProductCount(product.getProductCount() - 1);
				viewHolder.amountOfProducts.setText(String.valueOf(product.getProductCount()));
				viewHolder.totalPrice.setText(df.format(product.getTotalPrice()));
			}
		});
		
		
	}
	
	@Override
	public int getItemCount() {
		return productList.size();
	}
	
	public class ViewHolder extends RecyclerView.ViewHolder {
		private ImageView productImage;
		private TextView productName;
		private TextView currentPrice;
		private TextView regularPrice;
		private TextView amountOfProducts;
		private TextView totalPrice;
		private ImageButton add;
		private ImageButton remove;
		
		//maybe something for discounts
		
		public ViewHolder(@NonNull View itemView) {
			super(itemView);
			productImage = itemView.findViewById(R.id.productImage);
			productName = itemView.findViewById(R.id.productName);
			currentPrice = itemView.findViewById(R.id.individualPriceProduct);
			//regularprice
			amountOfProducts = itemView.findViewById(R.id.productCount);
			totalPrice = itemView.findViewById(R.id.totalPriceProduct);
			add = itemView.findViewById(R.id.imageButtonAdd);
			remove = itemView.findViewById(R.id.imageButtonRemove);
		}
	}
	
	
}
