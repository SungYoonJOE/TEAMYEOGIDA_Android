package com.example.hahaj.yeogida8;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ms.square.android.expandabletextview.ExpandableTextView;


public class Notify_Password extends AppCompatActivity {

    ExpandableTextView expandableTextView;
    String longText = "U.S. officials and Middle East analysts said Wednesday that an attack that killed four Americans at a U.S. Consulate in eastern Libya may have been planned by extremists and inspired by al-Qaeda.\n" +
            "\n" +
            "The U.S. Ambassador to Libya, J. Christopher Stevens, and three other Americans were killed Tuesday in an assault on the consulate in the city of Benghazi. President Obama strongly condemned the attack and pledged to bring the perpetrators to justice, vowing that “justice will be done.”\n" +
            "\n" +
            "The attack followed a violent protest at the U.S. Embassy in Cairo over a low-budget anti-Muslim film made in the United States, and it initially appeared that the assault on the Benghazi consulate was another spontaneous response. But senior U.S. officials and Middle East analysts raised questions Wednesday about the motivation for the Benghazi attack, noting that it involved the use of a rocket-propelled grenade and followed an al-Qaeda call to avenge the death of a senior Libyan member of the terrorist network.\n" +
            "\n" +
            "\n" +
            "Libyan officials and a witness said the attackers took advantage of a protest over the film to launch their assault.\n" +
            "\n" +
            "Stevens, 52, and the others appear to have been killed inside the temporary consulate, possibly by a rocket-propelled grenade, according to officials briefed on the assault.\n" +
            "\n" +
            "On Wednesday, administration officials described a fast-moving assault on the Benghazi compound, which quickly overwhelmed Libyan guards and U.S. security forces, and separated the Americans from the ambassador they were supposed to protect. U.S. personnel lost touch with Stevens just minutes into the attack, about 10 p.m. Benghazi time. They didn’t see him again until his body was returned to U.S. custody, sometime around dawn.\n" +
            "\n" +
            "“Frankly, we are not clear on the circumstances between the time he got separated from the group inside the burning building, to the time we were notified he was in Benghazi hospital,” a senior administration official who spoke on condition of anonymity told reporters. “We were not able to see him until his body was returned to us at the airport.”";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notify_password);

        expandableTextView = (ExpandableTextView)findViewById(R.id.expand_text_view);
        expandableTextView.setText(longText);
    }
}
