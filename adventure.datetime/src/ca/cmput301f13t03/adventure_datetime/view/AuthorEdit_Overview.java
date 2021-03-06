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

import java.util.UUID;

import ca.cmput301f13t03.adventure_datetime.R;
import ca.cmput301f13t03.adventure_datetime.model.Choice;
import ca.cmput301f13t03.adventure_datetime.model.Story;
import ca.cmput301f13t03.adventure_datetime.model.StoryFragment;
import ca.cmput301f13t03.adventure_datetime.serviceLocator.Locator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * 
 * Fragment owned by the AuthorEdit view
 * 
 * Shows a dynamically-created tree represetning
 * the nodes of the current story.
 * 
 * TODO : Everything.
 * 
 * @author James Finlay
 *
 */
public class AuthorEdit_Overview extends Fragment {
	
	private Button _btnAdd, _btnOther;
	private StoryFragment _fragment;
	private ListView _listView;
	private Story _story;
	
	public void setFragment(StoryFragment sf) {
		_fragment = sf;
		setUpView();
	}
	public void saveFragment() {}
	public void setStory(Story st) {
		_story = st;
		setUpView();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.overview_edit, container, false);
		
		_btnAdd = (Button) rootView.findViewById(R.id.add);
		_btnOther = (Button) rootView.findViewById(R.id.other);
		_listView = (ListView) rootView.findViewById(R.id.list_view);
		
		_btnAdd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				StoryFragment frag = new StoryFragment(_story.getId(),
						"", null);
				_story.addFragment(frag);
				Locator.getAuthorController().saveFragment(frag);
				Locator.getAuthorController().saveStory(_story);
				Toast.makeText(getActivity().getApplicationContext(),
						"Created fragment!", Toast.LENGTH_SHORT).show();
			}
		});
		_btnOther.setText("Add Choice");
		_btnOther.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Choice choice = new Choice("Unset", _fragment.getFragmentID());
				_fragment.addChoice(choice);
				Locator.getAuthorController().saveFragment(_fragment);
				Toast.makeText(getActivity().getApplicationContext(),
						"Choice created!", Toast.LENGTH_SHORT).show();
			}
		});
		
		setUpView();
		
		return rootView;
	}
	
	private void setUpView() {
		if (_listView ==  null) return;
		if (_story == null) return;
		if (_fragment == null) return;
		
	/*	for (UUID fragId : _story.) {
			
		} */
	}

}
