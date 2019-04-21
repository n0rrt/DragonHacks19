const int zIn = A1;
const int yIn = A2;
const int xIn = A3;
const int buttonPin = 7;
int buttonState = 0;
void setup() {
  Serial.begin(9600);
  pinMode(buttonPin, INPUT);
}

// the loop function runs over and over again forever
void loop() {
  int xOut = analogRead(xIn);
  int yOut = analogRead(yIn);
  int zOut = analogRead(zIn);
  String xOutStr = String(xOut);
  String yOutStr = String(yOut);
  String zOutStr = String(zOut);

  buttonState = digitalRead(buttonPin);

  if (buttonState == 1)
  {
    Serial.print(xOutStr);
    Serial.print("\t");
  
    Serial.print(yOutStr);
    Serial.print("\t");
  
    Serial.print(zOutStr);
    Serial.print("\t");
    Serial.print(buttonState);
    Serial.print("\n");
    delay(100);
  }
}
