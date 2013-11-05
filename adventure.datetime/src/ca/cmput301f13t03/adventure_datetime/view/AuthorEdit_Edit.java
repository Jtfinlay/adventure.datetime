/*
 *	Copyright (c) 2013 Andrew Fontaine, James Finlay, Jesse Tucker, Jacob Viau, and
 * 	Evan DeGraff
 *
 * 	Permission is hereby granted, free of charge, to any person obtaining a copy of
 * 	this software and associated documentation files (the "Software"), to deal in
 * 	the Software without restriction, including without limitation the rights to
 * 	use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * 	the Software, and to permit persons to whom the Software is furnished to do so,
 * 	subject to the following conditions:
 *
 * 	The above copyright notice and this permission notice shall be included in all
 * 	copies or substantial portions of the Software.
 *
 * 	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * 	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * 	FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * 	COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * 	IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * 	CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ca.cmput301f13t03.adventure_datetime.view;

import ca.cmput301f13t03.adventure_datetime.R;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AuthorEdit_Edit extends Fragment {

	private Button _btnChoices;
	private ImageButton _btnEditContent;
	private TextView _content;
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
			ViewGroup container, Bundle savedInstanceState) {

		/* TODO : This is all pretty much copy-paste from FragmentView.java. 
		 * Should probs not do that.
		 */

		View rootView = inflater.inflate(R.layout.fragment_edit, container, false);

		/** Layout items **/
		_btnChoices = (Button) rootView.findViewById(R.id.choices);
		_content = (TextView) rootView.findViewById(R.id.content);
		_btnEditContent = (ImageButton) rootView.findViewById(R.id.edit_content);
		
		//TODO: read actual content from model
		String tempText = ("The Bundesens say that Tardar Sauce's face " +
				"appears grumpy because of feline dwarfism and an under bite." +
				"She and her brother Pokey were born to normal parents with " +
				"'a flat face, bubble eyes, and a short tail'. Tardar Sauce " +
				"is undersized and has hind legs that 'are a bit different'." +
				"Ironically, Tardar Sauce is calm and 'actually really nice,'" +
				"whereas Pokey has a grumpy personality.\n\n");
		tempText += ("According to the Bundesens, Tardar Sauce is a normal" + 
				"cat '99% of the time'. Photo sessions are only once a week, " +
				"and handling by strangers is limited. At SXSW Tardar Sauce " +
				"made limited two-hour appearances each day as Grumpy Cat.");
		tempText += "Grumpy Cat appeared in episodes of the Friskies YouTube" + 
				"game show 'Will Kitty Play With It?' TMZ reported that " +
				"Friskies paid for flying first class, a private hotel room " +
				"with king-sized bed, a personal assistant, a chauffeur, and " +
				"unlimited Friskies food and bottled water. Bryan Bundesen "+
				"said the car was a BMW X5 with tinted windows. On March 22, "+
				"2013, Grumpy Cat traveled to New York City promoting the sho"+
				"w and appeared on Good Morning America and Anderson Live and"+
				"visited Time for a photoshoot. Michael Noer 'interviewed' Gr"+
				"umpy Cat for Forbes, released March 25.";
		_content.setText(tempText);
		
		/* Choices */
		_btnChoices.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().getActionBar().setSelectedNavigationItem(1);
				
				// TODO::JF Have Choice Editing here 
				
				final String[] sChoices = 
					{"Dance and Sing", "Cry a lot", "Go to RATT", 
						"Grade this app 100%", "Eat a gold brick wrapped in lettuce"};
				new AlertDialog.Builder(v.getContext())
				.setTitle("Actions")
				.setCancelable(true)
				.setItems(sChoices, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//TODO::JF Set new fragment
					}
				})
				.create().show();
			}			
		});
		
		/* Edit Content */
		_btnEditContent.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new AlertDialog.Builder(getActivity())
				.setView(getActivity().getLayoutInflater().inflate(R.layout.dialog_edit, null))
				.setPositiveButton("OK!", null)
				.setNegativeButton("Cancel", null)
				.create().show();
			}
		});
		
		
		return rootView;
	}
}