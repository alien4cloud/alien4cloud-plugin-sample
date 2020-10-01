import {ChangeDetectorRef, Component, ElementRef, Inject, OnInit, ViewChild} from '@angular/core';
import {AbstractControl, FormControl, ValidatorFn, Validators} from "@angular/forms";
import {SampleService} from "@app/core/services/sample.service";
import {animate, state, style, transition, trigger} from "@angular/animations";

@Component({
  selector: 'w4c-inventory-main',
  templateUrl: './hello.component.html',
  styleUrls: ['./hello.component.scss'],
  animations: [
  // this animation will operate when the message appears / disappears
  trigger('fade', [
    state('in', style({opacity: 1})),
    transition(':enter', [
      style({opacity: 0}),
      animate(800 )
    ]),
    transition(':leave',
      animate(600, style({opacity: 0})))
  ])
]
})
export class HelloComponent implements OnInit {

  // a form control to bind to name input
  inputField: FormControl = new FormControl();

  message: String;
  error: String;

  constructor(
    private sampleService: SampleService
  ) { }

  ngOnInit() {
  }

  submit() {
    this.error = undefined;
    this.sampleService.hello(this.inputField.value).subscribe(value => {
      this.inputField.reset();
      this.message = value;
      setTimeout(v => { this.message = undefined }, 2000);
    }, error => {
      this.error = error.message;
    });
  }
}
