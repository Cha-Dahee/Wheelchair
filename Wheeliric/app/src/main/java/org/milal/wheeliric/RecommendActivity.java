package org.milal.wheeliric;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class RecommendActivity extends AppCompatActivity {

    ArrayAdapter<CharSequence> adspin1, adspin2, adspin3, adspin4;
    String choice_do = "";
    String choice_do2 = "";
    String choice_do3 = "";
    String choice_do4 = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);

        final Spinner spin1 = (Spinner) findViewById(R.id.firstspinner);
        final Spinner spin2 = (Spinner) findViewById(R.id.secondspinner);
        final Spinner spin3 = (Spinner) findViewById(R.id.thirdspinner);
        final Spinner spin4 = (Spinner) findViewById(R.id.fourthspinner);

        final Button recommend = (Button) findViewById(R.id.goRecommend);

        adspin1 = ArrayAdapter.createFromResource(this, R.array.spinner_do, android.R.layout.simple_spinner_dropdown_item);
        adspin1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin1.setAdapter(adspin1);
        spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (adspin1.getItem(i).equals("식음료")) {
                    choice_do = "식음료";
                    adspin2 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_do_food, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(i).equals("놀거리")) {
                    choice_do = "놀거리";
                    adspin2 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_do_play, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(i).equals("병원")) {
                    choice_do = "병원";
                    adspin2 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_do_hospital, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin1.getItem(i).equals("생활")) {
                    choice_do = "생활";
                    adspin2 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_do_living, android.R.layout.simple_spinner_dropdown_item);
                    adspin2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin2.setAdapter(adspin2);
                    spin2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do2 = adspin2.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adspin3 = ArrayAdapter.createFromResource(this, R.array.spinner_place, android.R.layout.simple_spinner_dropdown_item);
        adspin3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin3.setAdapter(adspin3);
        spin3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                if (adspin3.getItem(i).equals("서울특별시")) {
                    choice_do3 = "서울특별시";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_seoul, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("부산광역시")) {
                    choice_do3 = "부산광역시";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_busan, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("대구광역시")) {
                    choice_do3 = "대구광역시";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_daegu, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("인천광역시")) {
                    choice_do3 = "인천광역시";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_incheon, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("광주광역시")) {
                    choice_do3 = "광주광역시";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_gwangju, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("대전광역시")) {
                    choice_do3 = "대전광역시";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_daejeon, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("울산광역시")) {
                    choice_do3 = "울산광역시";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_ulsan, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("세종특별자치시")) {
                    choice_do3 = "세종특별자치시";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_sejong, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("경기도")) {
                    choice_do3 = "경기도";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_gyeongi, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("강원도")) {
                    choice_do3 = "강원도";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_gangwon, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("충청북도")) {
                    choice_do3 = "충청북도";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_chungbuk, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("충청남도")) {
                    choice_do3 = "충청남도";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_chungnam, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("전라북도")) {
                    choice_do3 = "전라북도";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_jeonbuk, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("전라남도")) {
                    choice_do3 = "전라남도";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_jeonnam, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("경상북도")) {
                    choice_do3 = "경상북도";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_gyeongbuk, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("경상남도")) {
                    choice_do3 = "경상남도";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_gyeongnam, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else if (adspin3.getItem(i).equals("제주특별자치도")) {
                    choice_do3 = "제주특별자치도";
                    adspin4 = ArrayAdapter.createFromResource(RecommendActivity.this, R.array.spinner_jeju, android.R.layout.simple_spinner_dropdown_item);
                    adspin4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin4.setAdapter(adspin4);
                    spin4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                            choice_do4 = adspin4.getItem(i).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(getApplicationContext(), 어디로 갈지 넣어주셈);
                StringBuilder address = new StringBuilder(choice_do3 + " " + choice_do4);

                intent.putExtra("categories", choice_do2);
                intent.putExtra("address", address.toString());
                startActivity(intent);
                */
            }
        });
    }
}
